package com.cbec.entity.dto;

import com.cbec.entity.order.InboundItem;
import com.cbec.entity.order.InboundOrder;

import java.util.List;

public class InboundRequest {
    private InboundOrder order;
    private List<InboundItem> items;

    public InboundOrder getOrder() { return order; }
    public void setOrder(InboundOrder order) { this.order = order; }
    public List<InboundItem> getItems() { return items; }
    public void setItems(List<InboundItem> items) { this.items = items; }
}