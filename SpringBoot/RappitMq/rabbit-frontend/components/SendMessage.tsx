"use client";
import { useState } from "react";
import axios from "axios";
import { motion } from "framer-motion";

export default function SendMessage() {
  const [msg, setMsg] = useState("");

  const sendMessage = async () => {
    if (!msg) return;
    try {
      await axios.post("http://localhost:8080/api/messages", null, {
        params: { msg },
      });
      setMsg("");
    } catch (err) {
      console.error(err);
    }
  };

  return (
    <motion.div
      initial={{ opacity: 0, y: -20 }}
      animate={{ opacity: 1, y: 0 }}
      className="flex gap-2"
    >
      <input
        type="text"
        value={msg}
        onChange={(e) => setMsg(e.target.value)}
        placeholder="Type message..."
        className="flex-1 p-2 border rounded-md"
      />
      <button
        onClick={sendMessage}
        className="bg-blue-500 text-white px-4 rounded-md hover:bg-blue-600"
      >
        Send
      </button>
    </motion.div>
  );
}
