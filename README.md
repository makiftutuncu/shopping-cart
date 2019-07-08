# Shopping Cart Implementation

Implement an e-commerce shopping cart class.

## Rules

* Product has a title and price.
* Product belong to a category.
* Category may or may not have a parent category.
* Category has a title.
* Products are added to the Cart with quantity info.
* Campaigns exist for product price discounts.
* Campaigns are applicable to a product category.
* Campaign discount vary based on the number of products in the cart
* Coupons exist for cart discounts.
* Coupons have minimum cart amount constraint. If Cart amount is less than minimum, discount is not applied.
* Delivery Cost is Dynamic. Based on the number of deliveries and number of products.



## Cart

Implement a class that takes a cart and calculates the delivery cost.

```java
//sample creating a new category
Category food = new Category("food");

//products
Product apple = new Product("Apple", 100.0, category);
Product almond = new Product("Almonds", 150.0, category);

//Products can be added to a shopping cart with quantity
ShoppingCart cart = new ShoppingCart();
cart.addItem(apple,3);
cart.addItem(almond,1);
```

## Discounts

```java
//you can apply discounts to a category

//discount rules can be 20% on a category if bought more than 3 items
Campaign campaign1 = new Campaign(category,20.0,3,DiscountType.Rate);

//another campaign rule 50% on a category if bought more than 5 items
Campaign campaign2 = new Campaign(category,50.0,5,DiscountType.Rate);

//another campaign rule 5 TL amount discount on a category if bought more than  items
Campaign campaign3 = new Campaign(category,5.0,5,DiscountType.Amount);

//Cart should apply the maximum amount of discount to the cart.
cart.applyDiscounts(discount1,discount2,discount3);

//You can also apply a coupon to a cart

//Coupons have minimum amount. If the cart total is less than minimum amount, coupon is not applicable

//Coupon for 100 TL min purchase amount for a 10% discount
Coupon coupon = new Coupon(100, 10, DiscountType.Rate)

cart.applyCoupon(coupon)

// Campaign Discounts are applied first, Then Coupons.
```

## Delivery

To optimize our delivery Cost, we want to apply dynamic cargo pricing rules based on the number of deliveries and number of products in the cart.

Formula is (CostPerDelivery * NumberOfDeliveries) + (CostPerProduct * NumberOfProducts) + Fixed Cost

Fixed cost is 2.99 TL.

Implement a class that takes a cart and calculates the delivery cost.

```java
DeliveryCostCalculator deliveryCostCalculator = new DeliveryCostCalculator(costPerDelivery,costPerProduct,fixedCost);

double deliveryCostCalculator.calculateFor(cart)
```

 

NumberOfDeliveries is calculated by the number of distinct categories in the cart.

If cart has products that belong to two distinct categories, number of deliveries is 2.

NumberOfProducts is the number of different products in the cart. It is not the quantity of products.

## Cart Responsibilities

```java
double cart.getTotalAmountAfterDiscounts()
double cart.getCouponDiscount()
double cart.getCampaignDiscount()
double cart.getDeliveryCost()
```

Group Products by Category and Print the CategoryName, ProductName, Quantity, Unit Price, Total Price, Total Discount(coupon,campaign) applied

```java
cart.print()
```

At the final line print total amount and the delivery cost.

You will be asked additional requirements in the interview and how to implement them.

Please Drive your code with Tests and follow Object Oriented Design Principles (SRP, OCP)

