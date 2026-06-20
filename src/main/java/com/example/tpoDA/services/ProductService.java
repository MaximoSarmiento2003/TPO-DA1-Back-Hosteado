package com.example.tpoDA.services;

import com.example.tpoDA.dtos.product.ProductCreateDTO;
import com.example.tpoDA.dtos.product.ProductDetailResponseDTO;
import com.example.tpoDA.dtos.product.ProductResponseDTO;
import com.example.tpoDA.entities.*;
import com.example.tpoDA.exceptions.AppException;
import com.example.tpoDA.repositories.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final UserRepository userRepository;
    private final OwnerRepository ownerRepository;
    private final EmployeeRepository employeeRepository;
    private final ProductRepository productRepository;
    private final PhotoRepository photoRepository;
    private final PersonRepository personRepository;

    // ─── Crear producto ───────────────────────────────────────────────────────

    @Transactional
    public ProductResponseDTO createProduct(String email, ProductCreateDTO dto) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new AppException("Usuario no encontrado", HttpStatus.NOT_FOUND));

        Client client = user.getClient();
        Person person = client.getPerson();

        // Buscar o crear Owner
        Owner owner = ownerRepository.findById(person.getId())
                .orElseGet(() -> {
                    Employee verifier = employeeRepository.findRandom()
                            .orElseThrow(() -> new AppException(
                                    "No hay empleados disponibles", HttpStatus.INTERNAL_SERVER_ERROR));
                    Owner newOwner = Owner.builder()
                            .person(person)
                            .country(client.getCountry())
                            .verifier(verifier)
                            .financialVerification("no")
                            .judicialVerification("no")
                            .riskRating(3)
                            .build();
                    return ownerRepository.save(newOwner);
                });

        Employee reviewer = employeeRepository.findRandom()
                .orElseThrow(() -> new AppException(
                        "No hay empleados disponibles", HttpStatus.INTERNAL_SERVER_ERROR));

        String ownerConfirmed = (dto.getOwnerConfirmed() != null && dto.getOwnerConfirmed()) ? "si" : "no";

        // El producto nace como NO disponible — un empleado lo aprueba luego
        Product product;
        if ("art".equalsIgnoreCase(dto.getType())) {
            product = ArtProduct.builder()
                    .name(dto.getName())
                    .brand(dto.getBrand())
                    .quantity(dto.getQuantity() != null ? dto.getQuantity() : 1)
                    .isOwnerConfirmed(ownerConfirmed)
                    .history(dto.getHistory())
                    .date(LocalDate.now())
                    .available("no")          // pendiente de revisión
                    .catalogDescription(dto.getCatalogDescription())
                    .fullDescription(dto.getFullDescription() != null
                            ? dto.getFullDescription() : dto.getCatalogDescription())
                    .reviewer(reviewer)
                    .owner(owner)
                    .build();
        } else {
            product = NormalProduct.builder()
                    .name(dto.getName())
                    .brand(dto.getBrand())
                    .quantity(dto.getQuantity() != null ? dto.getQuantity() : 1)
                    .isOwnerConfirmed(ownerConfirmed)
                    .date(LocalDate.now())
                    .available("no")          // pendiente de revisión
                    .catalogDescription(dto.getCatalogDescription())
                    .fullDescription(dto.getFullDescription() != null
                            ? dto.getFullDescription() : dto.getCatalogDescription())
                    .reviewer(reviewer)
                    .owner(owner)
                    .build();
        }

        product = productRepository.save(product);

        // Guardar fotos
        int photoCount = 0;
        if (dto.getPhotosBase64() != null) {
            List<Photo> photos = new ArrayList<>();
            for (String b64 : dto.getPhotosBase64()) {
                if (b64 == null || b64.isBlank()) continue;
                try {
                    String clean = b64.replaceFirst("^data:image/[^;]+;base64,", "");
                    byte[] bytes = Base64.getDecoder().decode(clean);
                    photos.add(Photo.builder().product(product).image(bytes).build());
                } catch (IllegalArgumentException e) {
                    // foto inválida: ignorar
                }
            }
            photoRepository.saveAll(photos);
            photoCount = photos.size();
        }

        return toDTO(product, photoCount);
    }

    public ProductDetailResponseDTO getProductDetail(String email, Integer productId) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new AppException("Usuario no encontrado", HttpStatus.NOT_FOUND));
 
        Integer personId = user.getClient().getPerson().getId();
 
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new AppException("Producto no encontrado", HttpStatus.NOT_FOUND));
 
        // Verificar que el producto pertenece al usuario
        if (!product.getOwner().getId().equals(personId)) {
            throw new AppException("No autorizado", HttpStatus.FORBIDDEN);
        }
 
        // Fotos como base64
        java.util.List<String> photos = new java.util.ArrayList<>();
        if (product.getPhotos() != null) {
            for (com.example.tpoDA.entities.Photo ph : product.getPhotos()) {
                if (ph.getImage() != null) {
                    photos.add("data:image/jpeg;base64," + Base64.getEncoder().encodeToString(ph.getImage()));
                }
            }
        }
 
        // Insurance info
        ProductDetailResponseDTO.InsuranceInfo insuranceInfo = null;
        if (product.getInsurance() != null) {
            var ins = product.getInsurance();
            insuranceInfo = ProductDetailResponseDTO.InsuranceInfo.builder()
                    .policyNumber(ins.getPolicyNumber())
                    .company(ins.getCompany())
                    .combinedPolicy(ins.getCombinedPolicy())
                    .amount(ins.getAmount())
                    .build();
        }
 
        // Deposito info
        ProductDetailResponseDTO.DepositoInfo depositoInfo = null;
        if (product.getDeposito() != null) {
            var dep = product.getDeposito();
            depositoInfo = ProductDetailResponseDTO.DepositoInfo.builder()
                    .id(dep.getId())
                    .nombre(dep.getNombre())
                    .direccion(dep.getDireccion())
                    .ciudad(dep.getCiudad())
                    .pais(dep.getPais())
                    .telefono(dep.getTelefono())
                    .responsable(dep.getResponsable())
                    .horario(dep.getHorario())
                    .build();
        }
 
        // Name, brand, etc from subtype
        String type = "normal", name = "", brand = null, history = null;
        Integer quantity = null;
        if (product instanceof ArtProduct ap) {
            type = "art"; name = ap.getName(); brand = ap.getBrand();
            history = ap.getHistory(); quantity = ap.getQuantity();
        } else if (product instanceof NormalProduct np) {
            type = "normal"; name = np.getName(); brand = np.getBrand(); quantity = np.getQuantity();
        }
 
        // Reviewer name
        String reviewerName = null;

if (product.getReviewer() != null) {
    reviewerName = personRepository
            .findById(product.getReviewer().getId())
            .map(Person::getName)
            .orElse(null);
}
 
        return ProductDetailResponseDTO.builder()
                .id(product.getId())
                .type(type).name(name).brand(brand).quantity(quantity)
                .catalogDescription(product.getCatalogDescription())
                .fullDescription(product.getFullDescription())
                .history(history)
                .date(product.getDate())
                .available(product.getAvailable())
                .photosBase64(photos)
                .insurance(insuranceInfo)
                .deposito(depositoInfo)
                .reviewerName(reviewerName)
                .build();
    }

    // ─── Mis publicaciones (solo aprobados) ───────────────────────────────────

    public List<ProductResponseDTO> getMyApprovedProducts(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new AppException("Usuario no encontrado", HttpStatus.NOT_FOUND));

        Integer personId = user.getClient().getPerson().getId();

        if (!ownerRepository.existsById(personId)) return List.of();

        return productRepository.findApprovedByOwnerId(personId)
                .stream()
                .map(p -> toDTO(p, p.getPhotos() != null ? p.getPhotos().size() : 0))
                .toList();
    }

    // ─── Mapper interno ───────────────────────────────────────────────────────

    private ProductResponseDTO toDTO(Product p, int photoCount) {
        String type = "normal";
        String name = "";
        String brand = null;
        String history = null;
        Integer quantity = null;

        if (p instanceof ArtProduct ap) {
            type = "art";
            name = ap.getName();
            brand = ap.getBrand();
            history = ap.getHistory();
            quantity = ap.getQuantity();
        } else if (p instanceof NormalProduct np) {
            type = "normal";
            name = np.getName();
            brand = np.getBrand();
            quantity = np.getQuantity();
        }

        return ProductResponseDTO.builder()
                .id(p.getId())
                .type(type)
                .name(name)
                .brand(brand)
                .catalogDescription(p.getCatalogDescription())
                .fullDescription(p.getFullDescription())
                .history(history)
                .date(p.getDate())
                .available(p.getAvailable())
                .photoCount(photoCount)
                .build();
    }
}