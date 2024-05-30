**Giải thích ngắn gọn về các nguyên tắc, mô hình phát triển phần mềm và thực tiễn đang được áp dụng:**

- Nguyên tắc phát triển phần mềm:
  - Nguyên tắc thiết kế phần mềm để tạo ra mã nguồn dễ bảo trì, mở rộng và tái sử dụng. 
  - Nguyên tắc đảo ngược phụ thuộc (dependency injection).
  - Nguyên tắc khuyến khích sử dụng mã nguồn được tái sử dụng để giảm sự lặp lại và duy trì sự nhất quán trong mã nguồn.
- Mẫu phát triển phần mềm:
  - Mô hình kiến trúc RESTful: Dựa trên giao thức HTTP, sử dụng các phương thức HTTP như GET, POST, PUT và DELETE để quản lý các tài nguyên và tương tác giữa máy khách và máy chủ.
- Các phương pháp phát triển phần mềm:
  - Phương pháp phát triển phần mềm linh hoạt, trong đó quá trình phát triển được chia thành các vòng lặp ngắn

**Giải thích ngắn gọn về cấu trúc mã:**

- **Backend:**
  - Công nghệ: Spring Boot
  - RESTful API
  - Các folder phát triển chủ yếu là:
    - Controller: Chứa các tệp tin chịu trách nhiệm xử lý các yêu cầu từ client
    - Model: Chứa các tệp tin đại diện cho các data objects
    - Repository: Chứa các inteface repository kế thừa từ JpaRepository để thực hiện truy xuất database
    - Service: 
      - Chứa các Inteface của các lớp service để khi sử dụng service trong Controller thì chỉ cần tiêm interface vào giúp cho việc cập nhật code dễ dàng hơn
      - Folder Implement: 
        - Chứa các class service implement từ các interface service 
        - Các service tiêm vào các repository và chịu trách nhiệm thực hiện các chức năng cụ thể của ứng dụng
    - Utilities:
      - Chứa các lớp tiện ích để sử dụng trong toàn bộ ứng dụng
      - Chứa các lớp Resquest và Response trợ giúp cho việc gửi và nhận dữ liệu với client
    - Security
      - Chứa các cấu hình liên quan đến bảo mật, JWT,…

**Full CURL commands or Postman snapshots to verify the APIs (include full request endpoints, HTTP Headers and request payload if any):**
 ***link postman: https://api.postman.com/collections/29046226-b49f147a-f7da-4016-923c-9a6d48ee9b3c?access_key=PMAT-01HGGAAWJAAKMP8YWTMRVYAMR9 ***
 </br> *** Cần có user có Role là ADMIN để sử dụng các chức năng của admin như thêm, xóa, sửa sản phẩm,... ***
  ![image](https://github.com/phatlehuynh/SpringCommerce/assets/128290320/c685e6d8-e54e-468f-966d-8bdd5b4a99ad)

- Các pageIndex và pageSize đều có giá trị mặc định nếu không truyền
1) ***Product***
   1)  ***Get All Product*** 
      </br>![image](https://github.com/phatlehuynh/SpringCommerce/assets/128290320/9f3c5f29-c804-48be-90c2-eadcbd70adb7)
      </br>![image](https://github.com/phatlehuynh/SpringCommerce/assets/128290320/fc5a162f-60f5-47ae-a0a6-9f6c87ecb6d5)
   2) ***Get Page Product*** 
      </br>![image](https://github.com/phatlehuynh/SpringCommerce/assets/128290320/ca111fd8-205f-434d-956e-003f21e8bc2a)
   3) ***Get Page Product với CategoryId*** 
      </br>![image](https://github.com/phatlehuynh/SpringCommerce/assets/128290320/6b0e11e0-b381-4607-8a54-05c1db6edd9f)
   4) ***Get Page Product với keyword*** 
      </br>![image](https://github.com/phatlehuynh/SpringCommerce/assets/128290320/d2f51b1d-254c-486b-a6f1-cf950228657d)
   5) ***Get Page Filter*** 
      </br>![image](https://github.com/phatlehuynh/SpringCommerce/assets/128290320/b2ec7141-f05a-46c4-b30f-da9e87a65f55)
   6) ***Get Product By Id ***
      </br>![image](https://github.com/phatlehuynh/SpringCommerce/assets/128290320/f0d0e1f7-bef0-4907-8f7f-168359ed74b2)
   7) ***Get Product By UserId (Trả về các sản phẩm được đăng bởi tài khoản có id = userId)***
      </br>![image](https://github.com/phatlehuynh/SpringCommerce/assets/128290320/c8a456d4-b82f-482b-914b-767298d3c895)
   8) ***Insert Product***
      </br>![image](https://github.com/phatlehuynh/SpringCommerce/assets/128290320/3b42efae-ef1f-4082-9367-09e4fc4da4c4)
      </br>![image](https://github.com/phatlehuynh/SpringCommerce/assets/128290320/262c64ff-ef60-4708-90f0-31d55aa3ea94)
   9) ***Update Product***
      </br>![image](https://github.com/phatlehuynh/SpringCommerce/assets/128290320/a4bc4641-ef31-4786-8094-8e12aa145a59)
      </br>![image](https://github.com/phatlehuynh/SpringCommerce/assets/128290320/b23c3fd3-a5e3-4c5e-8c79-4071b7a0b74f)
   10) ***Delete Product***</br>
      </br>![image](https://github.com/phatlehuynh/SpringCommerce/assets/128290320/ae77482d-52dc-4c42-a68d-3b851242ce23)
2) ***Category***
   1)  ***Get All Category*** 
      </br>![image](https://github.com/phatlehuynh/SpringCommerce/assets/128290320/62835914-6838-4bcc-95a4-8d76c0a7d0f9)
   2)  ***Get Page Category*** 
      </br>![image](https://github.com/phatlehuynh/SpringCommerce/assets/128290320/948b1127-3a24-43f4-9ec6-120caa00c93d)
   3)  ***Get Category By Id*** 
      </br>![image](https://github.com/phatlehuynh/SpringCommerce/assets/128290320/fa584b6d-8757-417d-ba97-dcdd43b0d9c8)
      </br>![image](https://github.com/phatlehuynh/SpringCommerce/assets/128290320/9420666e-0a57-45ed-ab77-14156fb0047c)
   4)  ***Get Category By Id*** 
      </br>![image](https://github.com/phatlehuynh/SpringCommerce/assets/128290320/fa584b6d-8757-417d-ba97-dcdd43b0d9c8)
      </br>![image](https://github.com/phatlehuynh/SpringCommerce/assets/128290320/9420666e-0a57-45ed-ab77-14156fb0047c)
   5)  ***Get Category By Id of Parent Category*** 
      </br>![image](https://github.com/phatlehuynh/SpringCommerce/assets/128290320/842eb37c-02e3-4ed2-9c5a-c22863652ee4)
   6)  ***Insert Category*** 
      </br>![image](https://github.com/phatlehuynh/SpringCommerce/assets/128290320/3359562f-2524-4193-aec8-a796af9c511b)
      </br>![image](https://github.com/phatlehuynh/SpringCommerce/assets/128290320/617a5955-2603-4ab1-8354-0cca4f5ad21e)
   7)  ***Update Category*** 
      </br>![image](https://github.com/phatlehuynh/SpringCommerce/assets/128290320/a55d12b4-383b-4d3b-85b7-880f323d03e8)
      </br>![image](https://github.com/phatlehuynh/SpringCommerce/assets/128290320/c0e6e6ce-6e65-4f42-89e6-751c0334a570)
   8)  ***Delete Category*** 
      </br>![image](https://github.com/phatlehuynh/SpringCommerce/assets/128290320/5cb51b3c-9fbb-4850-b392-c7fe0457f307)
3) ***Cart***
   1)  ***Get All Cart*** 
      </br>![image](https://github.com/phatlehuynh/SpringCommerce/assets/128290320/9ad577b6-f5af-47f4-9cf5-e43ef0994a07)
      </br>![image](https://github.com/phatlehuynh/SpringCommerce/assets/128290320/29a2ab9e-b07c-4899-b949-0a8daab9d056)
   2)  ***Get Page Cart*** 
      </br>![image](https://github.com/phatlehuynh/SpringCommerce/assets/128290320/ba5ebcd6-980a-4c1c-8e5b-1eea67424720)
   3)  ***Get Cart By Id*** 
      </br>![image](https://github.com/phatlehuynh/SpringCommerce/assets/128290320/9cd26499-c9b1-4983-ab73-a64f6db5a24d)
   4)  ***Insert Cart*** 
      </br>![image](https://github.com/phatlehuynh/SpringCommerce/assets/128290320/160084bd-fcd3-47c5-88a7-3ce8c64aeb22)
      </br>![image](https://github.com/phatlehuynh/SpringCommerce/assets/128290320/4ea450be-7197-4cd1-882e-e474abfdc6da)
      </br>![image](https://github.com/phatlehuynh/SpringCommerce/assets/128290320/fa86ce0d-8506-4a6e-b4dc-aa035ddc51e2)
   5)  ***Add Product To Cart*** 
      </br>![image](https://github.com/phatlehuynh/SpringCommerce/assets/128290320/679d2439-6b41-4f5a-8ffa-e20eb7971c2a)
   6)  ***Remove Product From Cart*** 
      </br>![image](https://github.com/phatlehuynh/SpringCommerce/assets/128290320/89c374fb-42d8-45a1-b531-0e1ea1f2f17e)
   7)  ***Delete Cart*** 
      </br>![image](https://github.com/phatlehuynh/SpringCommerce/assets/128290320/527b7bd9-5a7f-44de-af2f-e9d7d549f2f0)
      </br>![image](https://github.com/phatlehuynh/SpringCommerce/assets/128290320/24019c85-c3b9-4cfd-90fc-8a5bbeb76462)
4) ***User***
   1)  ***Get All User*** 
      </br>![image](https://github.com/phatlehuynh/SpringCommerce/assets/128290320/716fd1c0-25da-418e-807f-951cf0f44cad)
   2)  ***Get Page User*** 
      </br>![image](https://github.com/phatlehuynh/SpringCommerce/assets/128290320/05eaaebd-86b9-489c-a1c8-363baaa0f5a4)
   3)  ***Get User By Id*** 
      </br>![image](https://github.com/phatlehuynh/SpringCommerce/assets/128290320/ca0379c1-00f3-4532-96f4-96188e7bf398)
   4)  ***User Register*** 
      </br>![image](https://github.com/phatlehuynh/SpringCommerce/assets/128290320/64c5f024-0e10-4c18-9379-2df17fad4552)
   5)  ***User Login*** 
      </br>![image](https://github.com/phatlehuynh/SpringCommerce/assets/128290320/ead4e8e9-94c3-4841-8e96-e90a98facf65)
   6)  ***Add Product To Cart Of User*** 
      </br>![image](https://github.com/phatlehuynh/SpringCommerce/assets/128290320/440919ab-963f-4d9c-97ae-7a0ee4651d17)
   7)  ***Remove Product To Cart Of User*** 
      </br>![image](https://github.com/phatlehuynh/SpringCommerce/assets/128290320/22e75197-014e-490f-91f7-795cbe3353c9)
   8)  ***Update User*** 
      </br>![image](https://github.com/phatlehuynh/SpringCommerce/assets/128290320/6417a41a-ac48-4689-b64b-fff1b45386ef)
5) ***Order***
   1)  ***Get All Order*** 
      </br>![image](https://github.com/phatlehuynh/SpringCommerce/assets/128290320/16b5583b-cb88-43f2-9df1-4b571d6ef4e3)
   2)  ***Get Page Order*** 
      </br>![image](https://github.com/phatlehuynh/SpringCommerce/assets/128290320/6c381c93-090a-4ebc-b1fd-368fe022a9a0)
   3)  ***Get Order By Id*** 
      </br>![image](https://github.com/phatlehuynh/SpringCommerce/assets/128290320/8c99b574-2cb4-4495-b63a-3fceec840acf)
   4)  ***Insert Order*** 
      </br>![image](https://github.com/phatlehuynh/SpringCommerce/assets/128290320/83cd18ce-82a5-4640-b648-ff3d156e3739)
   5)  ***Update Order Status*** 
      </br>![image](https://github.com/phatlehuynh/SpringCommerce/assets/128290320/43bafdac-7fa7-4189-a977-1f91f412a1c3)
   6)  ***Update Order By UserId*** 
      </br>![image](https://github.com/phatlehuynh/SpringCommerce/assets/128290320/03f83eed-3029-483a-ac0f-fd2d85480b27)

5) ***File***
   1)  ***Read Image*** 
      </br>![image](https://github.com/phatlehuynh/SpringCommerce/assets/128290320/5f5402cc-7858-4786-8fbb-4d21dbb13634)
   2)  ***Get All Uploads Image*** 
      </br>![image](https://github.com/phatlehuynh/SpringCommerce/assets/128290320/8ec1b42c-7519-479b-8989-e491a09d2934)
   3)  ***Upload Image*** 
      </br>![image](https://github.com/phatlehuynh/SpringCommerce/assets/128290320/4f335dc9-be79-4ebb-9f15-7725e03cba6c)























