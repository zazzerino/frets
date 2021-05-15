import { writable, Writable } from 'svelte/store';
import HomePage from './HomePage.svelte';
import { defaultUser, User } from "./user";
import type { Game } from "./game/game";

export const page: Writable<typeof HomePage> = writable(HomePage);

export const user: Writable<User> = writable(defaultUser);

export const game: Writable<Game> = writable(null);

export const games: Writable<Game[]> = writable([]);

export const players: Writable<User[]> = writable([]);
