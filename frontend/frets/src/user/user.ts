export interface User {
  id: number;
  name: string;
  gameId?: number;
}

export const defaultUser: User = {
  id: null,
  name: 'anon'
}
