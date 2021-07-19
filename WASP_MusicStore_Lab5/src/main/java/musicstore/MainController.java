package musicstore;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import musicstore.models.CartEntry;
import musicstore.models.Product;

@Controller
// difference between Controller and RestController
// RestController will assume all methods are annotated @ResponseBody and return JSON text
// Controller will listen to the different annotations and all Spring Boot to read jsps, html, etc.

public class MainController {
	@Autowired // This means to get the bean called userRepository
	private ProductRepository productRepository;

	public static HttpSession session() {
		ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
		return attr.getRequest().getSession(true); // true == allow create
	}
// Index
	@RequestMapping(path = "/")
	public String index() {
		return "thymeleaf/index";
	}
	
// Results
	@GetMapping(path = "/results")
	public String results(Model model) {
		HttpSession session = session();

		@SuppressWarnings("unchecked")
		List<CartEntry> entries = (List<CartEntry>) session.getAttribute("entries");

		if (entries == null) {
			session.setAttribute("entries", new ArrayList<CartEntry>());
		}

		model.addAttribute("product", productRepository.findAll());

		return "results";

	}	

	@GetMapping(path="/viewcartJSON", produces="application/json")
	@ResponseBody
	public List<CartEntry> cartJSON(){
		HttpSession session = session();
		@SuppressWarnings("unchecked")
		List<CartEntry> entries = (List<CartEntry>) session.getAttribute("entries");
		return entries;
	}
	
//Cart Page
	@GetMapping(path = "/viewcart")
	public String cart() {
		return "thymeleaf/cart";
	}

	
// Add to Cart
	@PostMapping(path = "/addtocart")
	public String addToCart(Model model, @RequestParam String productCode) {
		HttpSession session = session();

		@SuppressWarnings("unchecked")
		List<CartEntry> entries = (List<CartEntry>) session.getAttribute("entries");
		
		// If there's an existing CartEntry, update qty
		boolean updated = false;
		for (CartEntry entry : entries) {
			if (entry.getProduct().getCode() == productCode) {
				entry.setQty(entry.getQty() + 1);
				updated = true;
				break;
			}
		}
		// Otherwise find the right prod, and create a new CartEntry
		if (!updated) {
			Product p = productRepository.findOneByCode(productCode);
			entries.add(new CartEntry(p, 1));
		}
		model.addAttribute("entries",entries);
		
		return "redirect:viewcart";
	}

// Update Qty
	@PostMapping(path = "/updateqty")
	public String updateQty(@RequestParam String productCode, @RequestParam int qty) {
		@SuppressWarnings("unchecked")
		List<CartEntry> entries = (List<CartEntry>) session().getAttribute("entries");

		Iterator<CartEntry> iter = entries.iterator();
		while (iter.hasNext()) {
			CartEntry entry = iter.next();
			if (entry.getProduct().getCode() == productCode) {
				continue;
			}

			if (qty == 0) {
				iter.remove();
			} else {
				entry.setQty(qty);
			}
			break;
		}
		return "redirect:viewcart";
	}

// Edit Product Page
	@GetMapping(path="/editProduct")
	public String editProduct() {
		return "editProduct";
	}
	
// Edit
	@GetMapping(path = "/edit")
	public String edit(Model model, @RequestParam String productCode) {
		
		String code = (String) model.getAttribute("productCode");
		String description = (String) model.getAttribute("description");
		Double price = (Double) model.getAttribute("price");
		Product p = new Product(code,description,price);
		
		productRepository.save(p);
		
		return "redirect:editProduct";
	}
	
//ProductMaint JSON 
	@GetMapping(path = "/maintenanceJSON", produces="application/json")
	@ResponseBody
	public Iterable<Product> productMaintJSON() {
		Iterable<Product> prodList = productRepository.findAll();
		return prodList;
	}
	
//ProductMaint Page
	@GetMapping(path = "/maintenance", produces="application/json")
	public String productMaint(Model model) {
		Iterable<Product> prodList = productRepository.findAll();
		
		model.addAttribute("product", prodList);
		return "thymeleaf/productMaint";
	}

	@PostMapping(path = "/deleteProduct")
	public String deleteProd(@RequestParam String productCode) {
		productRepository.delete(productRepository.findOneByCode(productCode));
		return "redirect:maintenance";
	}

// Update products
	@PostMapping(path = "/updateproduct")
	public String updateProduct(Model model, @RequestParam(required = false) String productCode) {
		
		boolean exists = productRepository.existsById(productCode);
		if (exists) {
			session().setAttribute("productCode", productCode);
			String description = (String) model.getAttribute("description");
			Double price = (Double) model.getAttribute("price");
			Product p = new Product(productCode,description,price);
			
			productRepository.save(p);
		}
		else {
			session().setAttribute("error", "Product Not Found");
		}
		
		return "redirect:maintenance";
	}

}
