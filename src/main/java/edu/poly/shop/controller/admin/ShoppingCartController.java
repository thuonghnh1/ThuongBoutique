package edu.poly.shop.controller.admin;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import edu.poly.shop.domain.Product;
import edu.poly.shop.domain.CartItem;
import edu.poly.shop.service.ProductService;
import edu.poly.shop.service.ShoppingCartService;

@Controller
@RequestMapping("customer/cart")
public class ShoppingCartController {

    @Autowired
    private ShoppingCartService cartService;

    @Autowired
    private ProductService productService;

//    @PostMapping("add/{productId}")
//    public String addToCart(@PathVariable("productId") Long productId, @RequestParam("quantity") int quantity, ModelMap model) {
//        Optional<Product> productOpt = productService.findById(productId);
//        if (productOpt.isPresent()) {
//            Product product = productOpt.get();
//            CartItem cartItem = new CartItem();
//            cartItem.setProductId(product.getProductId());
//            cartItem.setName(product.getName());
//            cartItem.setQuantity(quantity);
//            cartItem.setUnitPrice(product.getUnitPrice());
//            cartService.addItem(cartItem);
//        }
//        return "redirect:/customer/cart/view";
//    }
    
    @PostMapping("add/{productId}")
    public String addToCart(@PathVariable("productId") Long productId, @RequestParam("quantity") int quantity, RedirectAttributes redirectAttributes) {
        Optional<Product> productOpt = productService.findById(productId);
        if (productOpt.isPresent()) {
            Product product = productOpt.get();
            
            // Lấy số lượng sản phẩm hiện có trong giỏ hàng
            int existingQuantity = cartService.getQuantityInCart(productId);

            // Tổng số lượng cần thêm vào giỏ hàng
            int totalQuantity = existingQuantity + quantity;

            // Kiểm tra xem tổng số lượng cần thêm có vượt quá số lượng sản phẩm trong kho không
            if (product.getQuantity() >= totalQuantity) {
                CartItem cartItem = new CartItem();
                cartItem.setProductId(product.getProductId());
                cartItem.setName(product.getName());
                cartItem.setQuantity(quantity);
                cartItem.setUnitPrice(product.getUnitPrice());
                cartService.addItem(cartItem);
                return "redirect:/customer/cart/view";
            } else {
                redirectAttributes.addFlashAttribute("errorMessage",  "Số lượng sản phẩm không đủ!");
                return "redirect:/customer/products/detail/" + productId;
            }
        }
        return "redirect:/customer/cart/view";
    }


    @GetMapping("view")
    public String viewCart(ModelMap model) {
        model.addAttribute("cartItems", cartService.getItems());
        return "customer/cart/view";
    }

    @GetMapping("remove/{productId}")
    public String removeFromCart(@PathVariable("productId") Long productId) {
        cartService.removeItem(productId);
        return "redirect:/customer/cart/view";
    }

    @GetMapping("clear")
    public String clearCart() {
        cartService.clear();
        return "redirect:/customer/cart/view";
    }
}