"use client";

import { useEffect, useState } from "react";
import axios from "axios";

interface Message {
  id: number;
  content: string;
  createdAt: string;
}

export default function MessageList() {
  const [messages, setMessages] = useState<Message[]>([]);

  // Fetch messages from DB on page load
  useEffect(() => {
    const fetchMessages = async () => {
      try {
        const res = await axios.get("http://localhost:8080/api/messages/db");
        setMessages(res.data);
      } catch (err) {
        console.error(err);
      }
    };
    fetchMessages();
  }, []);

  return (
    <div className="flex flex-col space-y-4">
      {messages.map((msg) => (
        <div
          key={msg.id}
          className="bg-white/70 backdrop-blur-md p-4 rounded-xl shadow-md max-w-md self-start hover:shadow-xl transition-shadow duration-300"
        >
          <p className="text-gray-800">{msg.content}</p>
          <span className="text-gray-500 text-xs mt-1 block">
            {new Date(msg.createdAt).toLocaleString()}
          </span>
        </div>
      ))}
    </div>
  );
}
