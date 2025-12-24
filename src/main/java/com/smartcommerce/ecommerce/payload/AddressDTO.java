package com.smartcommerce.ecommerce.payload;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(name = "AddressDTO", description = "Thông tin chi tiết về địa chỉ giao hàng")
public class AddressDTO {

    @Schema(description = "ID của địa chỉ (tự động tạo bởi hệ thống)", example = "1", accessMode = Schema.AccessMode.READ_ONLY)
    private Long addressId;

    @Schema(description = "Tên đường/Số nhà", example = "Số 1 Đại Cồ Việt", requiredMode = Schema.RequiredMode.REQUIRED)
    private String street;

    @Schema(description = "Tên tòa nhà hoặc khu chung cư", example = "Tòa nhà B1")
    private String buildingName;

    @Schema(description = "Tên thành phố", example = "Hà Nội", requiredMode = Schema.RequiredMode.REQUIRED)
    private String city;

    @Schema(description = "Tên tỉnh/thành trực thuộc trung ương", example = "Hà Nội")
    private String state;

    @Schema(description = "Tên quốc gia", example = "Việt Nam")
    private String country;

    @Schema(description = "Mã bưu điện", example = "100000")
    private String pincode;
}