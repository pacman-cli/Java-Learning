import React, { useEffect, useState } from "react";
import axios from "axios";
import LiveMessages from "./LiveMessages";

const MessageList = () => {
  const [messages, setMessages] = useState([]);

  // Fetch messages from DB on load
  const fetchMessages = async () => {
    try {
      const response = await axios.get(
        "http://localhost:8080/api/messages/all",
      );
      setMessages(response.data);
    } catch (err) {
      console.error("Error fetching messages:", err);
    }
  };

  useEffect(() => {
    fetchMessages();
  }, []);

  // Handle new live message from WebSocket
  const handleNewMessage = (message) => {
    setMessages((prev) => [...prev, { content: message }]);
  };

  return (
    <div>
      <LiveMessages onMessage={handleNewMessage} />
      <ul>
        {messages.map((msg, index) => (
          <li key={index}>{msg.content}</li>
        ))}
      </ul>
    </div>
  );
};

export default MessageList;
