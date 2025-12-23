"use client";

export default function ServiceStatus() {
  return (
    <div className="bg-white rounded-lg shadow-md p-6">
      <h2 className="text-2xl font-bold text-gray-800 mb-4">Service Status</h2>
      <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-4">
        <div className="border rounded-lg p-4 text-center">
          <h3 className="font-semibold text-gray-800">Gateway</h3>
          <p className="text-sm text-gray-600">Port: 8080</p>
          <span className="status-online">ONLINE</span>
        </div>
        <div className="border rounded-lg p-4 text-center">
          <h3 className="font-semibold text-gray-800">User Service</h3>
          <p className="text-sm text-gray-600">Port: 8081</p>
          <span className="status-offline">OFFLINE</span>
        </div>
        <div className="border rounded-lg p-4 text-center">
          <h3 className="font-semibold text-gray-800">Order Service</h3>
          <p className="text-sm text-gray-600">Port: 8082</p>
          <span className="status-pending">PENDING</span>
        </div>
        <div className="border rounded-lg p-4 text-center">
          <h3 className="font-semibold text-gray-800">Inventory Service</h3>
          <p className="text-sm text-gray-600">Port: 8083</p>
          <span className="status-offline">OFFLINE</span>
        </div>
      </div>
    </div>
  );
}
