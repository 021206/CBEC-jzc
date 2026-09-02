package com.cbec.entity.dto;

import com.cbec.entity.order.OutboundItem;
import com.cbec.entity.order.OutboundOrder;

import java.util.List;

public class OutboundRequest {
    private OutboundOrder order;
    private List<OutboundItem> items;

    public OutboundOrder getOrder() { return order; }
    public void setOrder(OutboundOrder order) { this.order = order; }
    public List<OutboundItem> getItems() { return items; }
    public void setItems(List<OutboundItem> items) { this.items = items; }
}