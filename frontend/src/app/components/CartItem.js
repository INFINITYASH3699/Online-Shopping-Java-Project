"use client";

import React from "react";
import formatCurrency from "../utils/formatCurrency";

export default function CartItem({ item, onQuantityChange, onRemove }) {
  return (
    <div className="flex items-center justify-between bg-white p-4 rounded shadow mb-4">
      <div>
        <h3 className="text-lg font-bold">{item.name}</h3>
        <p>{formatCurrency(item.price)}</p>
      </div>
      <div className="flex items-center">
        <input
          type="number"
          value={item.quantity}
          min="1"
          className="border w-16 text-center"
          onChange={(e) => onQuantityChange(item.id, parseInt(e.target.value))}
        />
        <button
          onClick={() => onRemove(item.id)}
          className="ml-4 bg-red-500 text-white px-4 py-2 rounded hover:bg-red-600"
        >
          Remove
        </button>
      </div>
    </div>
  );
}
