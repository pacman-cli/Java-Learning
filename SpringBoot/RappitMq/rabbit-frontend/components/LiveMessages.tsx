"use client";
import { useEffect, useState } from "react";
import SockJS from "sockjs-client";
import { Client, IMessage } from "@stomp/stompjs";

export default function LiveMessages() {
  const [messages, setMessages] = useState<string[]>([]);

  useEffect(() => {
    // Create SockJS connection
    const socket = new SockJS("http://localhost:8080/ws");

    // Create STOMP client
    const client = new Client({
      // Provide factory returning WebSocket-like object
      webSocketFactory: () => socket as unknown as WebSocket,
      debug: (str) => console.log(str),
      reconnectDelay: 5000,
      onConnect: () => {
        client.subscribe("/topic/messages", (msg: IMessage) => {
          setMessages((prev) => [...prev, msg.body]);
        });
      },
    });

    client.activate();

    return () => {
      client.deactivate();
    };
  }, []);

  return (
    <div className="bg-white p-4 rounded-xl shadow-md">
      <h2 className="text-lg font-bold mb-2">Live Messages</h2>
      <ul className="space-y-1">
        {messages.map((m, i) => (
          <li key={i} className="p-2 border-b last:border-b-0">
            {m}
          </li>
        ))}
      </ul>
    </div>
  );
}
