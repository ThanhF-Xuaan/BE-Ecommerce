package com.smartcommerce.ecommerce.controller;

import com.smartcommerce.ecommerce.payload.AddressDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

import java.util.List;

@Tag(name = "Address", description = "Quản lý địa chỉ giao hàng của người dùng")
public interface AddressApi {

    @Operation(summary = "Tạo địa chỉ mới", description = "Tạo địa chỉ cho người dùng đang đăng nhập dựa trên JWT Token.")
    @ApiResponse(responseCode = "210", description = "Tạo thành công")
    @ApiResponse(responseCode = "400", description = "Dữ liệu không hợp lệ")
    ResponseEntity<AddressDTO> createAddress(AddressDTO addressDTO);

    @Operation(summary = "Lấy tất cả địa chỉ", description = "Lấy danh sách toàn bộ địa chỉ trong hệ thống (Quyền Admin).")
    ResponseEntity<List<AddressDTO>> getAddresses();

    @Operation(summary = "Lấy địa chỉ theo ID", description = "Tìm kiếm thông tin địa chỉ cụ thể qua ID.")
    @ApiResponse(responseCode = "200", description = "Thành công")
    @ApiResponse(responseCode = "404", description = "Không tìm thấy địa chỉ")
    ResponseEntity<AddressDTO> getAddressById(@Parameter(description = "ID của địa chỉ") Long addressId);

    @Operation(summary = "Lấy địa chỉ của người dùng hiện tại", description = "Lấy danh sách các địa chỉ gắn liền với User đang đăng nhập.")
    ResponseEntity<List<AddressDTO>> getUserAddresses();

    @Operation(summary = "Cập nhật địa chỉ", description = "Sửa đổi thông tin địa chỉ đã tồn tại.")
    ResponseEntity<AddressDTO> updateAddress(Long addressId, AddressDTO addressDTO);

    @Operation(summary = "Xóa địa chỉ", description = "Xóa địa chỉ khỏi hệ thống theo ID.")
    ResponseEntity<String> deleteAddress(Long addressId);
}
