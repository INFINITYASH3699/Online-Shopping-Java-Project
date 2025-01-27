export default function formatCurrency(amount) {
  return `₹${amount.toLocaleString("en-IN", { maximumFractionDigits: 2 })}`;
}
