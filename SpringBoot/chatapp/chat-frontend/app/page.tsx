import ChatPage from "./ChatPage";

interface Props {
  searchParams: { roomId?: string; username?: string };
}

export default function Page({ searchParams }: Props) {
  const roomId = searchParams.roomId ? parseInt(searchParams.roomId) : 1;
  const username = searchParams.username || "Anonymous";

  return <ChatPage roomId={roomId} username={username} />;
}
