"use client";

import { useState } from "react";
import axios from "axios";

export default function MessageInput() {
  const [message, setMessage] = useState("");

  const sendMessage = async () => {
    if (!message.trim()) return;
    try {
      await axios.post("http://localhost:8080/api/messages", { msg: message });
      setMessage("");
    } catch (err) {
      console.error(err);
    }
  };

  const handleKeyPress = (e: React.KeyboardEvent) => {
    if (e.key === "Enter") sendMessage();
  };

  return (
    <div className="flex mt-4 w-full max-w-3xl mx-auto space-x-2 px-4">
      <input
        type="text"
        value={message}
        onChange={(e) => setMessage(e.target.value)}
        onKeyDown={handleKeyPress}
        className="flex-1 p-3 rounded-xl border border-gray-300 focus:outline-none focus:ring-2 focus:ring-blue-400"
        placeholder="Type your message..."
      />
      <button
        onClick={sendMessage}
        className="px-5 py-3 bg-blue-500 text-white rounded-xl hover:bg-blue-600 transition"
      >
        Send
      </button>
    </div>
  );
}
