"use client";

import React from "react";
import formatCurrency from "../utils/formatCurrency";

export default function OrderCard({ order }) {
  return (
    <div className="bg-white shadow rounded-lg p-4 flex flex-col">
      <h2 className="text-lg font-bold mb-2">Order #{order.id}</h2>
      <p className="mb-2">Status: {order.status}</p>
      <p className="mb-2">Total: {formatCurrency(order.totalPrice)}</p>
      <h3 className="font-bold mt-4">Products:</h3>
      <ul className="list-disc list-inside">
        {Object.entries(order.products).map(([productId, productDetails]) => (
          <li key={productId}>
            {productDetails.name} - {productDetails.quantity} pcs
          </li>
        ))}
      </ul>
    </div>
  );
}
