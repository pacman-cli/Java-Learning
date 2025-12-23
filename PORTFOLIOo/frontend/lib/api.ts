const API_BASE = process.env.NEXT_PUBLIC_API_BASE_URL || 'http://localhost:8080/api';

export async function fetchJSON<T>(path: string, init?: RequestInit): Promise<T> {
  const url = `${API_BASE}${path}`;
  const headers: HeadersInit = {
    'Content-Type': 'application/json',
    ...(init?.headers || {}),
  };
  const res = await fetch(url, { ...(init || {}), headers });
  if (!res.ok) {
    throw new Error(`API error ${res.status}`);
  }
  return res.json() as Promise<T>;
}

export type Skill = { id: number; name: string; level: number; category?: string };
export type Project = { id: number; title: string; description: string; tags: string[]; repoUrl?: string; demoUrl?: string; imageUrl?: string };
export type Education = { id: number; school: string; degree: string; field: string; startYear: number; endYear?: number };
export type Experience = { id: number; company: string; role: string; startDate: string; endDate?: string; description: string };
