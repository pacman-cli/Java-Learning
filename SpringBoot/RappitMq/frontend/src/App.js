import React from "react";
import SendMessage from "./components/SendMessage";
import MessageList from "./components/MessageList";

function App() {
  return (
    <div>
      <h1>RabbitMQ Chat</h1>
      <SendMessage />
      <MessageList />
    </div>
  );
}

export default App;
