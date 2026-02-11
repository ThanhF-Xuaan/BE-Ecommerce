package com.smartcommerce.ecommerce.controller;

import com.smartcommerce.ecommerce.payload.AddressDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Tag(name = "Address", description = "APIs quản lý địa chỉ giao hàng (Admin & User)")
@SecurityRequirement(name = "bearerAuth") // Yêu cầu JWT cho toàn bộ Controller này
public interface AddressApi {

    @Operation(summary = "Tạo địa chỉ mới", description = "Thêm địa chỉ giao hàng cho người dùng đang đăng nhập.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Tạo địa chỉ thành công",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = AddressDTO.class))),
            @ApiResponse(responseCode = "400", description = "Dữ liệu đầu vào không hợp lệ (Validation Failed)"),
            @ApiResponse(responseCode = "401", description = "Chưa đăng nhập (Unauthorized)")
    })
    ResponseEntity<AddressDTO> createAddress(@Parameter(description = "Thông tin địa chỉ cần tạo", required = true)
                                             @Valid @RequestBody AddressDTO addressDTO);

    @Operation(summary = "Lấy tất cả địa chỉ (Admin)", description = "Admin xem danh sách toàn bộ địa chỉ trong hệ thống.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lấy danh sách thành công"),
            @ApiResponse(responseCode = "403", description = "Không có quyền Admin (Forbidden)")
    })
    ResponseEntity<List<AddressDTO>> getAddresses();

    @Operation(summary = "Lấy chi tiết địa chỉ", description = "Lấy thông tin chi tiết của một địa chỉ theo ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Tìm thấy địa chỉ"),
            @ApiResponse(responseCode = "404", description = "Không tìm thấy địa chỉ với ID cung cấp")
    })
    ResponseEntity<AddressDTO> getAddressById(@Parameter(description = "ID của địa chỉ", example = "1")
                                              @PathVariable Long addressId);

    @Operation(summary = "Lấy danh sách địa chỉ của tôi", description = "User lấy danh sách các địa chỉ của chính mình.")
    @ApiResponse(responseCode = "200", description = "Thành công")
    ResponseEntity<List<AddressDTO>> getCurrentUserAddresses();

    @Operation(summary = "Cập nhật địa chỉ", description = "Cập nhật thông tin địa chỉ dựa trên ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cập nhật thành công"),
            @ApiResponse(responseCode = "400", description = "Dữ liệu không hợp lệ"),
            @ApiResponse(responseCode = "404", description = "Không tìm thấy địa chỉ"),
            @ApiResponse(responseCode = "403", description = "Không có quyền sửa địa chỉ này")
    })
    ResponseEntity<AddressDTO> updateAddress(@Parameter(description = "ID địa chỉ cần sửa", example = "1") @PathVariable Long addressId,
                                             @Parameter(description = "Thông tin cập nhật") @Valid @RequestBody AddressDTO addressDTO);

    @Operation(summary = "Xóa địa chỉ (Admin)", description = "Admin xóa bất kỳ địa chỉ nào khỏi hệ thống.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Xóa thành công"),
            @ApiResponse(responseCode = "404", description = "Không tìm thấy địa chỉ")
    })
    ResponseEntity<String> deleteAddressByAdmin(@Parameter(description = "ID địa chỉ cần xóa") @PathVariable Long addressId);

    @Operation(summary = "Xóa địa chỉ của tôi", description = "User tự xóa địa chỉ của chính mình.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Xóa thành công"),
            @ApiResponse(responseCode = "403", description = "Không thể xóa địa chỉ của người khác"),
            @ApiResponse(responseCode = "404", description = "Không tìm thấy địa chỉ")
    })
    ResponseEntity<String> deleteAddressOfCurrentUser(@Parameter(description = "ID địa chỉ cần xóa") @PathVariable Long addressId);
}
