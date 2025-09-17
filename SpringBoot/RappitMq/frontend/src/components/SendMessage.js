import React, { useState } from "react";
import axios from "axios";

const SendMessage = () => {
  const [msg, setMsg] = useState("");

  const sendMessage = async () => {
    if (!msg) return;
    try {
      await axios.post(`http://localhost:8080/api/messages?msg=${msg}`);
      setMsg("");
    } catch (err) {
      console.error("Error sending message:", err);
    }
  };

  return (
    <div>
      <input
        type="text"
        value={msg}
        onChange={(e) => setMsg(e.target.value)}
        placeholder="Type a message..."
      />
      <button onClick={sendMessage}>Send</button>
    </div>
  );
};

export default SendMessage;
