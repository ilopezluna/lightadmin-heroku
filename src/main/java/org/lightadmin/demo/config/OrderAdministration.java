package org.lightadmin.demo.config;

import org.lightadmin.api.config.annotation.Administration;
import org.lightadmin.api.config.builder.*;
import org.lightadmin.api.config.unit.EntityMetadataConfigurationUnit;
import org.lightadmin.api.config.unit.FieldSetConfigurationUnit;
import org.lightadmin.api.config.unit.FiltersConfigurationUnit;
import org.lightadmin.api.config.unit.ScreenContextConfigurationUnit;
import org.lightadmin.api.config.utils.EntityNameExtractor;
import org.lightadmin.api.config.utils.FieldValueRenderer;
import org.lightadmin.demo.model.LineItem;
import org.lightadmin.demo.model.Order;

import java.util.Set;

import static com.google.common.collect.Sets.newLinkedHashSet;
import static java.lang.String.format;
import static org.springframework.util.StringUtils.collectionToDelimitedString;

@SuppressWarnings("unused")
@Administration(Order.class)
public class OrderAdministration {

    public static EntityMetadataConfigurationUnit configuration(EntityMetadataConfigurationUnitBuilder configurationBuilder) {
        return configurationBuilder
                .nameExtractor(orderNameExtractor())
                .singularName("Order")
                .pluralName("Orders").build();
    }

    public static ScreenContextConfigurationUnit screenContext(ScreenContextConfigurationUnitBuilder screenContextBuilder) {
        return screenContextBuilder.screenName("Orders Administration").build();
    }

    public static FieldSetConfigurationUnit listView(FieldSetConfigurationUnitBuilder fragmentBuilder) {
        return fragmentBuilder
                .dynamic("customer.firstname").caption("Customer First Name")
                .dynamic("customer.lastname").caption("Customer Last Name")
                .field("billingAddress").caption("Billing Address")
                .field("shippingAddress").caption("Shipping Address")
                .renderable(lineItemsFieldValueRenderer()).caption("Order Items").build();
    }

    public static FieldSetConfigurationUnit showView(final FieldSetConfigurationUnitBuilder fragmentBuilder) {
        return fragmentBuilder
                .field("customer").caption("Customer")
                .field("billingAddress").caption("Billing Address")
                .field("shippingAddress").caption("Shipping Address")
                .renderable(lineItemsFieldValueRenderer()).caption("Order Items").build();
    }

    public static FieldSetConfigurationUnit quickView(final FieldSetConfigurationUnitBuilder fragmentBuilder) {
        return fragmentBuilder
                .field("customer").caption("Customer")
                .field("shippingAddress").caption("Shipping Address")
                .renderable(lineItemsFieldValueRenderer()).caption("Order Items").build();
    }

    public static FieldSetConfigurationUnit formView(final PersistentFieldSetConfigurationUnitBuilder fragmentBuilder) {
        return fragmentBuilder
                .field("customer").caption("Customer")
                .field("billingAddress").caption("Billing Address")
                .field("shippingAddress").caption("Shipping Address")
                .field("lineItems").caption("Order Items").build();
    }

    public static FiltersConfigurationUnit filters(final FiltersConfigurationUnitBuilder filterBuilder) {
        return filterBuilder
                .filter("Customer", "customer")
                .filter("Billing Address", "billingAddress")
                .filter("Shipping Address", "shippingAddress")
                .filter("Order items", "lineItems").build();
    }

    private static EntityNameExtractor<Order> orderNameExtractor() {
        return new EntityNameExtractor<Order>() {
            @Override
            public String apply(final Order order) {
                return format("Order %s for $%d", order.getCustomer().getFirstname(), order.getTotal().intValue());
            }
        };
    }

    private static FieldValueRenderer<Order> lineItemsFieldValueRenderer() {
        return new FieldValueRenderer<Order>() {
            @Override
            public String apply(final Order order) {
                Set<String> lineItems = newLinkedHashSet();
                for (LineItem lineItem : order.getLineItems()) {
                    lineItems.add(format("Product %s in amount of %d", lineItem.getProduct().getName(), lineItem.getAmount()));
                }
                return collectionToDelimitedString(lineItems, ", ");
            }
        };
    }
}
