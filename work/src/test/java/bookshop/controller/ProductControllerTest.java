package bookshop.controller;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.hasSize;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.eq;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;

import bookshop.dto.request.PageRequest;
import bookshop.dto.request.ProductCreateDto;
import bookshop.dto.request.ProductUpdateDto;
import bookshop.dto.response.PageResponse;
import bookshop.exceptions.BusinessException;
import bookshop.exceptions.ProductNotFoundException;
import bookshop.models.Product;
import bookshop.services.serviceInterface.ProductService;

/**
 * Comprehensive integration tests for ProductController REST endpoints
 * Uses MockMvc to test HTTP requests and responses
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("ProductController REST API Tests")
class ProductControllerTest {

    private MockMvc mockMvc;

    private ObjectMapper objectMapper;

    @Mock
    private ProductService productService;

    @InjectMocks
    private ProductController productController;

    private Product testProduct;
    private ProductCreateDto createDto;
    private ProductUpdateDto updateDto;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(productController).build();
        objectMapper = new ObjectMapper();
        
        testProduct = new Product();
        testProduct.setProductId(1);
        testProduct.setName("Test Book");
        testProduct.setPrice(new BigDecimal("29.99"));
        testProduct.setCategoryId(1);

        createDto = new ProductCreateDto();
        createDto.setName("New Book");
        createDto.setPrice(new BigDecimal("19.99"));
        createDto.setCategoryId(1);

        updateDto = new ProductUpdateDto();
        updateDto.setName("Updated Book");
        updateDto.setPrice(new BigDecimal("24.99"));
        updateDto.setCategoryId(1);
    }

    @Nested
    @DisplayName("GET /api/products - Get All Products")
    // Tests for retrieving paginated list of products with various parameters and edge cases
    class GetAllProductsTests {

        @Test
        @DisplayName("Should return paginated products with default parameters")
        void testGetAllProducts_WithDefaults_ReturnsOk() throws Exception {
            // Arrange
            Product product2 = new Product(2, "Book 2", new BigDecimal("39.99"), 1);
            List<Product> products = Arrays.asList(testProduct, product2);
            PageResponse<Product> pageResponse = new PageResponse<>(products, 0, 10, 2);

            when(productService.getAllProducts(any(PageRequest.class))).thenReturn(pageResponse);

            // Act & Assert
            mockMvc.perform(get("/api/products"))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.content", hasSize(2)))
                    .andExpect(jsonPath("$.content[0].productId").value(1))
                    .andExpect(jsonPath("$.content[0].name").value("Test Book"))
                    .andExpect(jsonPath("$.content[0].price").value(29.99))
                    .andExpect(jsonPath("$.page").value(0))
                    .andExpect(jsonPath("$.size").value(10))
                    .andExpect(jsonPath("$.totalElements").value(2))
                    .andExpect(jsonPath("$.first").value(true))
                    .andExpect(jsonPath("$.last").value(true));

            verify(productService, times(1)).getAllProducts(any(PageRequest.class));
        }

        @Test
        @DisplayName("Should return products with custom pagination parameters")
        void testGetAllProducts_WithCustomParams_ReturnsOk() throws Exception {
            // Arrange
            List<Product> products = Arrays.asList(testProduct);
            PageResponse<Product> pageResponse = new PageResponse<>(products, 1, 5, 10);

            when(productService.getAllProducts(any(PageRequest.class))).thenReturn(pageResponse);

            // Act & Assert
            mockMvc.perform(get("/api/products")
                            .param("page", "1")
                            .param("size", "5")
                            .param("sortBy", "name")
                            .param("direction", "DESC"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.page").value(1))
                    .andExpect(jsonPath("$.size").value(5))
                    .andExpect(jsonPath("$.totalElements").value(10));

            verify(productService, times(1)).getAllProducts(any(PageRequest.class));
        }

        @Test
        @DisplayName("Should return empty page when no products exist")
        void testGetAllProducts_EmptyResult_ReturnsOk() throws Exception {
            // Arrange
            PageResponse<Product> emptyPage = new PageResponse<>(Arrays.asList(), 0, 10, 0);
            when(productService.getAllProducts(any(PageRequest.class))).thenReturn(emptyPage);

            // Act & Assert
            mockMvc.perform(get("/api/products"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.content", hasSize(0)))
                    .andExpect(jsonPath("$.totalElements").value(0))
                    .andExpect(jsonPath("$.empty").value(true));
        }
    }

    @Nested
    @DisplayName("GET /api/products/{id} - Get Product By ID")
    class GetProductByIdTests {

        @Test
        @DisplayName("Should return product when ID exists")
        void testGetProductById_WithValidId_ReturnsOk() throws Exception {
            // Arrange
            when(productService.getProductById(1)).thenReturn(Optional.of(testProduct));

            // Act & Assert
            mockMvc.perform(get("/api/products/1"))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.productId").value(1))
                    .andExpect(jsonPath("$.name").value("Test Book"))
                    .andExpect(jsonPath("$.price").value(29.99))
                    .andExpect(jsonPath("$.categoryId").value(1));

            verify(productService, times(1)).getProductById(1);
        }

        @Test
        @DisplayName("Should return 404 when product not found")
        void testGetProductById_WithInvalidId_ReturnsNotFound() throws Exception {
            // Arrange
            when(productService.getProductById(999)).thenReturn(Optional.empty());

            // Act & Assert
            mockMvc.perform(get("/api/products/999"))
                    .andExpect(status().isNotFound());

            verify(productService, times(1)).getProductById(999);
        }

        @Test
        @DisplayName("Should handle product with multiple fields")
        void testGetProductById_CompleteProduct_ReturnsAllFields() throws Exception {
            // Arrange
            testProduct.setCategoryName("Electronics");
            when(productService.getProductById(1)).thenReturn(Optional.of(testProduct));

            // Act & Assert
            mockMvc.perform(get("/api/products/1"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.productId").value(1))
                    .andExpect(jsonPath("$.name").value("Test Book"))
                    .andExpect(jsonPath("$.price").value(29.99))
                    .andExpect(jsonPath("$.categoryId").value(1))
                    .andExpect(jsonPath("$.categoryName").value("Electronics"));
        }
    }

    @Nested
    @DisplayName("GET /api/products/category/{categoryId} - Get Products By Category")
    class GetProductsByCategoryTests {

        @Test
        @DisplayName("Should return products for valid category with default pagination")
        void testGetProductsByCategory_WithDefaults_ReturnsOk() throws Exception {
            // Arrange
            List<Product> products = Arrays.asList(testProduct);
            PageResponse<Product> pageResponse = new PageResponse<>(products, 0, 20, 1);

            when(productService.getProductsByCategory(eq(1), any(PageRequest.class)))
                    .thenReturn(pageResponse);

            // Act & Assert
            mockMvc.perform(get("/api/products/category/1"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.content", hasSize(1)))
                    .andExpect(jsonPath("$.content[0].categoryId").value(1))
                    .andExpect(jsonPath("$.size").value(20));

            verify(productService, times(1)).getProductsByCategory(eq(1), any(PageRequest.class));
        }

        @Test
        @DisplayName("Should return products with custom pagination")
        void testGetProductsByCategory_WithCustomParams_ReturnsOk() throws Exception {
            // Arrange
            List<Product> products = Arrays.asList(testProduct);
            PageResponse<Product> pageResponse = new PageResponse<>(products, 0, 10, 1);

            when(productService.getProductsByCategory(eq(1), any(PageRequest.class)))
                    .thenReturn(pageResponse);

            // Act & Assert
            mockMvc.perform(get("/api/products/category/1")
                            .param("page", "0")
                            .param("size", "10")
                            .param("sortBy", "price")
                            .param("direction", "DESC"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.size").value(10));
        }

        @Test
        @DisplayName("Should return empty page for category with no products")
        void testGetProductsByCategory_EmptyResult_ReturnsOk() throws Exception {
            // Arrange
            PageResponse<Product> emptyPage = new PageResponse<>(Arrays.asList(), 0, 20, 0);
            when(productService.getProductsByCategory(eq(5), any(PageRequest.class)))
                    .thenReturn(emptyPage);

            // Act & Assert
            mockMvc.perform(get("/api/products/category/5"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.content", hasSize(0)))
                    .andExpect(jsonPath("$.empty").value(true));
        }
    }

    @Nested
    @DisplayName("GET /api/products/search - Search Products")
    class SearchProductsTests {

        @Test
        @DisplayName("Should return products matching search keyword")
        void testSearchProducts_WithValidKeyword_ReturnsOk() throws Exception {
            // Arrange
            List<Product> products = Arrays.asList(testProduct);
            PageResponse<Product> pageResponse = new PageResponse<>(products, 0, 20, 1);

            when(productService.searchProducts(eq("Test"), any(PageRequest.class)))
                    .thenReturn(pageResponse);

            // Act & Assert
            mockMvc.perform(get("/api/products/search")
                            .param("keyword", "Test"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.content", hasSize(1)))
                    .andExpect(jsonPath("$.content[0].name", containsString("Test")));

            verify(productService, times(1)).searchProducts(eq("Test"), any(PageRequest.class));
        }

        @Test
        @DisplayName("Should handle search with custom pagination")
        void testSearchProducts_WithPagination_ReturnsOk() throws Exception {
            // Arrange
            List<Product> products = Arrays.asList(testProduct);
            PageResponse<Product> pageResponse = new PageResponse<>(products, 1, 10, 15);

            when(productService.searchProducts(eq("Book"), any(PageRequest.class)))
                    .thenReturn(pageResponse);

            // Act & Assert
            mockMvc.perform(get("/api/products/search")
                            .param("keyword", "Book")
                            .param("page", "1")
                            .param("size", "10"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.page").value(1))
                    .andExpect(jsonPath("$.size").value(10));
        }

        @Test
        @DisplayName("Should return empty result when no products match")
        void testSearchProducts_NoMatches_ReturnsEmpty() throws Exception {
            // Arrange
            PageResponse<Product> emptyPage = new PageResponse<>(Arrays.asList(), 0, 20, 0);
            when(productService.searchProducts(eq("NoMatch"), any(PageRequest.class)))
                    .thenReturn(emptyPage);

            // Act & Assert
            mockMvc.perform(get("/api/products/search")
                            .param("keyword", "NoMatch"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.content", hasSize(0)));
        }
    }

    @Nested
    @DisplayName("GET /api/products/price-range - Get Products By Price Range")
    class GetProductsByPriceRangeTests {

        @Test
        @DisplayName("Should return products within price range")
        void testGetProductsByPriceRange_ValidRange_ReturnsOk() throws Exception {
            // Arrange
            List<Product> products = Arrays.asList(testProduct);
            PageResponse<Product> pageResponse = new PageResponse<>(products, 0, 20, 1);

            when(productService.getProductsByPriceRange(
                    eq(new BigDecimal("20.00")),
                    eq(new BigDecimal("30.00")),
                    any(PageRequest.class)))
                    .thenReturn(pageResponse);

            // Act & Assert
            mockMvc.perform(get("/api/products/price-range")
                            .param("minPrice", "20.00")
                            .param("maxPrice", "30.00"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.content", hasSize(1)))
                    .andExpect(jsonPath("$.content[0].price").value(29.99));

            verify(productService, times(1)).getProductsByPriceRange(
                    any(BigDecimal.class),
                    any(BigDecimal.class),
                    any(PageRequest.class));
        }

        @Test
        @DisplayName("Should handle price range with pagination and sorting")
        void testGetProductsByPriceRange_WithSorting_ReturnsOk() throws Exception {
            // Arrange
            List<Product> products = Arrays.asList(testProduct);
            PageResponse<Product> pageResponse = new PageResponse<>(products, 0, 10, 5);

            when(productService.getProductsByPriceRange(
                    any(BigDecimal.class),
                    any(BigDecimal.class),
                    any(PageRequest.class)))
                    .thenReturn(pageResponse);

            // Act & Assert
            mockMvc.perform(get("/api/products/price-range")
                            .param("minPrice", "10.00")
                            .param("maxPrice", "50.00")
                            .param("page", "0")
                            .param("size", "10")
                            .param("sortBy", "price")
                            .param("direction", "ASC"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.size").value(10));
        }

        @Test
        @DisplayName("Should return empty result when no products in range")
        void testGetProductsByPriceRange_NoProducts_ReturnsEmpty() throws Exception {
            // Arrange
            PageResponse<Product> emptyPage = new PageResponse<>(Arrays.asList(), 0, 20, 0);
            when(productService.getProductsByPriceRange(
                    any(BigDecimal.class),
                    any(BigDecimal.class),
                    any(PageRequest.class)))
                    .thenReturn(emptyPage);

            // Act & Assert
            mockMvc.perform(get("/api/products/price-range")
                            .param("minPrice", "100.00")
                            .param("maxPrice", "200.00"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.content", hasSize(0)));
        }
    }

    @Nested
    @DisplayName("POST /api/products - Create Product")
    class CreateProductTests {

        @Test
        @DisplayName("Should create product with valid data and return 201")
        void testCreateProduct_WithValidData_ReturnsCreated() throws Exception {
            // Arrange
            Product savedProduct = new Product();
            savedProduct.setProductId(10);
            savedProduct.setName(createDto.getName());
            savedProduct.setPrice(createDto.getPrice());
            savedProduct.setCategoryId(createDto.getCategoryId());

            when(productService.createProduct(any(Product.class))).thenReturn(savedProduct);

            // Act & Assert
            mockMvc.perform(post("/api/products")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(createDto)))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.productId").value(10))
                    .andExpect(jsonPath("$.name").value("New Book"))
                    .andExpect(jsonPath("$.price").value(19.99))
                    .andExpect(jsonPath("$.categoryId").value(1));

            verify(productService, times(1)).createProduct(any(Product.class));
        }

        @Test
        @DisplayName("Should return 400 when name is missing")
        void testCreateProduct_WithMissingName_ReturnsBadRequest() throws Exception {
            // Arrange
            createDto.setName(null);

            // Act & Assert
            mockMvc.perform(post("/api/products")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(createDto)))
                    .andExpect(status().isBadRequest());

            verify(productService, never()).createProduct(any(Product.class));
        }

        @Test
        @DisplayName("Should return 400 when name is too short")
        void testCreateProduct_WithShortName_ReturnsBadRequest() throws Exception {
            // Arrange
            createDto.setName("A");

            // Act & Assert
            mockMvc.perform(post("/api/products")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(createDto)))
                    .andExpect(status().isBadRequest());

            verify(productService, never()).createProduct(any(Product.class));
        }

        @Test
        @DisplayName("Should return 400 when price is missing")
        void testCreateProduct_WithMissingPrice_ReturnsBadRequest() throws Exception {
            // Arrange
            createDto.setPrice(null);

            // Act & Assert
            mockMvc.perform(post("/api/products")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(createDto)))
                    .andExpect(status().isBadRequest());

            verify(productService, never()).createProduct(any(Product.class));
        }

        @Test
        @DisplayName("Should return 400 when price is zero")
        void testCreateProduct_WithZeroPrice_ReturnsBadRequest() throws Exception {
            // Arrange
            createDto.setPrice(BigDecimal.ZERO);

            // Act & Assert
            mockMvc.perform(post("/api/products")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(createDto)))
                    .andExpect(status().isBadRequest());

            verify(productService, never()).createProduct(any(Product.class));
        }

        @Test
        @DisplayName("Should return 400 when categoryId is missing")
        void testCreateProduct_WithMissingCategoryId_ReturnsBadRequest() throws Exception {
            // Arrange
            createDto.setCategoryId(null);

            // Act & Assert
            mockMvc.perform(post("/api/products")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(createDto)))
                    .andExpect(status().isBadRequest());

            verify(productService, never()).createProduct(any(Product.class));
        }

        @Test
        @DisplayName("Should return 400 when categoryId is zero")
        void testCreateProduct_WithZeroCategoryId_ReturnsBadRequest() throws Exception {
            // Arrange
            createDto.setCategoryId(0);

            // Act & Assert
            mockMvc.perform(post("/api/products")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(createDto)))
                    .andExpect(status().isBadRequest());

            verify(productService, never()).createProduct(any(Product.class));
        }

        @Test
        @DisplayName("Should handle business exception from service")
        void testCreateProduct_WithBusinessException_ReturnsBadRequest() throws Exception {
            // Arrange
            when(productService.createProduct(any(Product.class)))
                    .thenThrow(new BusinessException("Product name already exists"));

            // Act & Assert
            mockMvc.perform(post("/api/products")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(createDto)))
                    .andExpect(status().isBadRequest());
        }
    }

    @Nested
    @DisplayName("PUT /api/products/{id} - Update Product")
    class UpdateProductTests {

        @Test
        @DisplayName("Should update product with valid data and return 200")
        void testUpdateProduct_WithValidData_ReturnsOk() throws Exception {
            // Arrange
            Product updatedProduct = new Product();
            updatedProduct.setProductId(1);
            updatedProduct.setName(updateDto.getName());
            updatedProduct.setPrice(updateDto.getPrice());
            updatedProduct.setCategoryId(updateDto.getCategoryId());

            when(productService.getProductById(1)).thenReturn(Optional.of(testProduct));
            when(productService.updateProduct(any(Product.class))).thenReturn(updatedProduct);

            // Act & Assert
            mockMvc.perform(put("/api/products/1")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(updateDto)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.productId").value(1))
                    .andExpect(jsonPath("$.name").value("Updated Book"))
                    .andExpect(jsonPath("$.price").value(24.99));

            verify(productService, times(1)).getProductById(1);
            verify(productService, times(1)).updateProduct(any(Product.class));
        }

        @Test
        @DisplayName("Should return 404 when updating non-existent product")
        void testUpdateProduct_WithInvalidId_ReturnsNotFound() throws Exception {
            // Arrange
            when(productService.getProductById(999)).thenReturn(Optional.empty());

            // Act & Assert
            mockMvc.perform(put("/api/products/999")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(updateDto)))
                    .andExpect(status().isNotFound());

            verify(productService, times(1)).getProductById(999);
            verify(productService, never()).updateProduct(any(Product.class));
        }

        @Test
        @DisplayName("Should return 400 when update data is invalid")
        void testUpdateProduct_WithInvalidData_ReturnsBadRequest() throws Exception {
            // Arrange
            updateDto.setName("A"); // Too short

            // Act & Assert
            mockMvc.perform(put("/api/products/1")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(updateDto)))
                    .andExpect(status().isBadRequest());

            verify(productService, never()).getProductById(anyInt());
            verify(productService, never()).updateProduct(any(Product.class));
        }

        @Test
        @DisplayName("Should return 400 when price is invalid")
        void testUpdateProduct_WithInvalidPrice_ReturnsBadRequest() throws Exception {
            // Arrange
            updateDto.setPrice(BigDecimal.ZERO);

            // Act & Assert
            mockMvc.perform(put("/api/products/1")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(updateDto)))
                    .andExpect(status().isBadRequest());

            verify(productService, never()).updateProduct(any(Product.class));
        }
    }

    @Nested
    @DisplayName("DELETE /api/products/{id} - Delete Product")
    class DeleteProductTests {

        @Test
        @DisplayName("Should delete product and return 204")
        void testDeleteProduct_WithValidId_ReturnsNoContent() throws Exception {
            // Arrange
            doNothing().when(productService).deleteProduct(1);

            // Act & Assert
            mockMvc.perform(delete("/api/products/1"))
                    .andExpect(status().isNoContent());

            verify(productService, times(1)).deleteProduct(1);
        }

        @Test
        @DisplayName("Should handle deletion of non-existent product")
        void testDeleteProduct_WithInvalidId_ReturnsError() throws Exception {
            // Arrange
            doThrow(new ProductNotFoundException("Product not found with ID: 999"))
                    .when(productService).deleteProduct(999);

            // Act & Assert
            mockMvc.perform(delete("/api/products/999"))
                    .andExpect(status().isNotFound());

            verify(productService, times(1)).deleteProduct(999);
        }

        @Test
        @DisplayName("Should verify delete is called exactly once")
        void testDeleteProduct_CallsServiceOnce() throws Exception {
            // Arrange
            doNothing().when(productService).deleteProduct(5);

            // Act
            mockMvc.perform(delete("/api/products/5"))
                    .andExpect(status().isNoContent());

            // Assert
            verify(productService, times(1)).deleteProduct(5);
            verify(productService, never()).deleteProduct(anyInt());
        }
    }

    @Nested
    @DisplayName("Request Content Type Tests")
    class ContentTypeTests {

        @Test
        @DisplayName("Should accept JSON content type for POST")
        void testCreateProduct_WithJsonContentType_Success() throws Exception {
            // Arrange
            Product savedProduct = new Product(10, "New Book", new BigDecimal("19.99"), 1);
            when(productService.createProduct(any(Product.class))).thenReturn(savedProduct);

            // Act & Assert
            mockMvc.perform(post("/api/products")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(createDto)))
                    .andExpect(status().isCreated())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON));
        }

        @Test
        @DisplayName("Should return JSON content type for GET")
        void testGetProduct_ReturnsJsonContentType() throws Exception {
            // Arrange
            when(productService.getProductById(1)).thenReturn(Optional.of(testProduct));

            // Act & Assert
            mockMvc.perform(get("/api/products/1"))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON));
        }
    }

    @Nested
    @DisplayName("Edge Cases and Error Handling")
    class EdgeCaseTests {

        @Test
        @DisplayName("Should handle large page numbers gracefully")
        void testGetAllProducts_WithLargePageNumber_ReturnsEmpty() throws Exception {
            // Arrange
            PageResponse<Product> emptyPage = new PageResponse<>(Arrays.asList(), 1000, 10, 5);
            when(productService.getAllProducts(any(PageRequest.class))).thenReturn(emptyPage);

            // Act & Assert
            mockMvc.perform(get("/api/products")
                            .param("page", "1000"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.content", hasSize(0)));
        }

        @Test
        @DisplayName("Should handle very long product names")
        void testCreateProduct_WithMaxLengthName_Success() throws Exception {
            // Arrange
            String maxName = "A".repeat(255);
            createDto.setName(maxName);
            Product savedProduct = new Product(20, maxName, new BigDecimal("19.99"), 1);
            when(productService.createProduct(any(Product.class))).thenReturn(savedProduct);

            // Act & Assert
            mockMvc.perform(post("/api/products")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(createDto)))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.name").value(maxName));
        }

        @Test
        @DisplayName("Should reject product names longer than 255 characters")
        void testCreateProduct_WithTooLongName_ReturnsBadRequest() throws Exception {
            // Arrange
            String tooLongName = "A".repeat(256);
            createDto.setName(tooLongName);

            // Act & Assert
            mockMvc.perform(post("/api/products")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(createDto)))
                    .andExpect(status().isBadRequest());
        }

        @Test
        @DisplayName("Should handle products with very high prices")
        void testCreateProduct_WithLargePrice_Success() throws Exception {
            // Arrange
            createDto.setPrice(new BigDecimal("99999999.99"));
            Product savedProduct = new Product(30, "Expensive Item", new BigDecimal("99999999.99"), 1);
            when(productService.createProduct(any(Product.class))).thenReturn(savedProduct);

            // Act & Assert
            mockMvc.perform(post("/api/products")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(createDto)))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.price").value(99999999.99));
        }
    }
}
