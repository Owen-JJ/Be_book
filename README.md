# Book Management API

## 📌 Giới thiệu
Đây là API quản lý sách được xây dựng để đáp ứng yêu cầu bài kiểm tra Backend Developer. API hỗ trợ các tính năng CRUD cho sách và tác giả, bao gồm:
- Thêm, cập nhật, xoá sách
- Truy vấn danh sách sách với bộ lọc
- Lấy thông tin sách theo ID (hỗ trợ truy vấn thông tin tác giả đi kèm)
- Xoá tác giả (tùy chọn)
- Đảm bảo validation, xử lý lỗi hợp lý

## 🛠 Công nghệ sử dụng
- **Ngôn ngữ & Framework**: Java với Spring Boot
- **Cơ sở dữ liệu**: MySQL
- **ORM**: Hibernate (JPA)
- **Công cụ khác**: ModelMapper, Spring Data JPA

## 📁 Cấu trúc thư mục
```
project-root/
│── src/main/java/org/be/
│   ├── controllers/        # Chứa các Rest API controller
│   ├── dtos/               # Chứa các DTO dùng để truyền dữ liệu
│   ├── entities/           # Chứa các entity class ánh xạ database
│   ├── repositories/       # Chứa các interface truy vấn database
│   ├── services/           # Chứa logic nghiệp vụ
│   ├── services/Impl/      # Chứa các implement của service
│   ├── BEBookApplication.java  # Main Application
│
│── src/main/resources/
│   ├── application.properties  # Cấu hình database, server
│
│── test/java/org/be/services/
│   ├── BookServiceTest.java # Test các nghiệp vụ trong service
│
│── pom.xml                     # Cấu hình Maven
```

## 🔧 Cài đặt & Chạy ứng dụng
### 📌 Yêu cầu hệ thống
- **Java 17+**
- **Maven 3+**
- **MySQL/PostgreSQL/SQLite**

### 🚀 Cách chạy ứng dụng
1. **Cấu hình database**
   - Mở file `src/main/resources/application.properties`
   - Cập nhật thông tin kết nối:
     ```properties
     spring.datasource.url=jdbc:mysql://localhost:3306/book_db
     spring.datasource.username=root
     spring.datasource.password=your_password
     spring.jpa.hibernate.ddl-auto=update
     ```
2. **Build project**
   ```sh
   mvn clean install
   ```
3. **Chạy ứng dụng**
   ```sh
   mvn spring-boot:run
   ```

## 📌 API Endpoints
### 📖 1. Lấy thông tin sách theo ID
- **GET /api/books/{id}**
- **Query Params**:
  - `includeAuthor` (boolean, tùy chọn, mặc định `false`): Nếu `true`, trả về thông tin tác giả.
- **Response:**
  ```json
  {
    "status": "200",
    "message": "Sách được tìm thấy!",
    "data": {
      "id": 1,
      "title": "Mắt Biếc",
      "authorId": 1,
      "isbn": "978-604-123456-7",
      "publishedDate": "2024-01-01",
      "price": 100000
    }
  }
  ```

### ✍️ 2. Tạo sách mới
- **POST /api/books**
- **Request Body:**
  ```json
  {
    "title": "Mắt Biếc",
    "authorId": 1,
    "isbn": "978-604-123456-7",
    "publishedDate": "2024-01-01",
    "price": 100000
  }
  ```

### 🔄 3. Cập nhật sách
- **PUT /api/books/{id}**
- **Request Body:** *(Tương tự như tạo sách)*

### 🗑️ 4. Xóa sách
- **DELETE /api/books/{id}**

### 🗑️ 5. Xóa tác giả (Tùy chọn)
- **DELETE /api/authors/{id}**

## 🛠 Validation & Error Handling
- **ISBN phải là duy nhất.**
- **Giá sách phải lớn hơn 0.**
- **Trả về lỗi 404 nếu sách hoặc tác giả không tồn tại.**
- **Xử lý lỗi LazyInitializationException bằng FetchType hoặc @Transactional.**

## ✅ Chạy Test
Chạy test bằng lệnh:
```sh
mvn test
```

## 📢 Ghi chú
- Cấu hình database có thể thay đổi tùy theo môi trường sử dụng.
- Để tránh lỗi `LazyInitializationException`, có thể dùng `@Transactional` hoặc thay đổi `FetchType.LAZY` thành `FetchType.EAGER`.

## 👨‍💻 Tác giả
Dự án được phát triển bởi **NguyenTruongGiang**.

