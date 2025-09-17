"use client";

import React, { useEffect, useState } from "react";
import { Client } from "@stomp/stompjs";
import SockJS from "sockjs-client";

interface ChatMessage {
  roomId: number;
  sender: string;
  content: string;
}

interface ChatPageProps {
  roomId: number;
  username: string;
}

export default function ChatPage({ roomId, username }: ChatPageProps) {
  const [stompClient, setStompClient] = useState<Client | null>(null);
  const [messages, setMessages] = useState<ChatMessage[]>([]);
  const [input, setInput] = useState("");

  useEffect(() => {
    const socket = new SockJS("http://localhost:8080/ws-chat");

    const client = new Client({
      webSocketFactory: () => socket,
      reconnectDelay: 5000,
      debug: (str) => console.log(str),
    });

    client.onConnect = () => {
      console.log("✅ Connected to STOMP");

      client.subscribe(`/topic/chat/${roomId}`, (msg) => {
        const receivedMessage = JSON.parse(msg.body) as ChatMessage;
        setMessages((prev) => [...prev, receivedMessage]);
      });
    };

    client.activate();
    setStompClient(client);

    return () => {
      client.deactivate();
    };
  }, [roomId]);

  const sendMessage = () => {
    if (stompClient && stompClient.connected) {
      const message: ChatMessage = { roomId, sender: username, content: input };
      stompClient.publish({
        destination: "/app/chat.sendMessage",
        body: JSON.stringify(message),
      });
      setInput("");
    } else {
      console.error("❌ Not connected to STOMP");
    }
  };

  return (
    <div>
      <h2>Room: {roomId}</h2>
      <div style={{ maxHeight: "300px", overflowY: "auto" }}>
        {messages.map((m, idx) => (
          <div key={idx}>
            <strong>{m.sender}:</strong> {m.content}
          </div>
        ))}
      </div>
      <input
        type="text"
        value={input}
        onChange={(e) => setInput(e.target.value)}
        placeholder="Type your message..."
      />
      <button onClick={sendMessage}>Send</button>
    </div>
  );
}
