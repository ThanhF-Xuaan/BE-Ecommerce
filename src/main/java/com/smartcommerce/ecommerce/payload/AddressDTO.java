package com.smartcommerce.ecommerce.payload;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@Schema(name = "AddressDTO", description = "Thông tin chi tiết về địa chỉ giao hàng")
public class AddressDTO {

    @Schema(description = "ID của địa chỉ", example = "1", accessMode = Schema.AccessMode.READ_ONLY)
    Long addressId;

    @NotBlank(message = "{address.city.notblank}")
    @Size(max = 30, message = "{address.city.size}")
    @Schema(description = "Tỉnh hoặc Thành phố", example = "Hà Nội", requiredMode = Schema.RequiredMode.REQUIRED)
    String city;

    @NotBlank(message = "{address.ward.notblank}")
    @Size(max = 30, message = "{address.ward.size}")
    @Schema(description = "Phường hoặc Xã", example = "Phường Dịch Vọng Hậu", requiredMode = Schema.RequiredMode.REQUIRED)
    String ward;

    @NotBlank(message = "{address.street.notblank}")
    @Size(min = 5, max = 255, message = "{address.street.size}")
    @Schema(description = "Số nhà, ngõ, tên đường cụ thể", example = "Số 123, Ngõ 45, Đường Xuân Thuỷ", requiredMode = Schema.RequiredMode.REQUIRED)
    String streetDetail;

    @Schema(description = "Tên tòa nhà hoặc khu chung cư (nếu có)", example = "Tòa nhà Indochina Plaza")
    String buildingName;

    @Size(min = 5, message = "{address.pincode.size}")
    @Schema(description = "Mã bưu điện", example = "100000")
    @NotBlank(message = "{address.pincode.notblank}")
    String pincode;

    @Schema(description = "Đánh dấu là địa chỉ mặc định", example = "true")
    Boolean isDefault;
}