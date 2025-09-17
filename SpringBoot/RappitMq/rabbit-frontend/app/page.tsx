import Navbar from "../components/Navbar";
import MessageInput from "../components/MessageInput";
import MessageList from "../components/MessageList";

export default function Home() {
  return (
    <div className="min-h-screen bg-gradient-to-b from-purple-100 via-pink-50 to-blue-50">
      <Navbar />
      <MessageList />
      <MessageInput />
    </div>
  );
}
