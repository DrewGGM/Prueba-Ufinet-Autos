import client from './client';
import type { AuthResponse } from '../types';

export const register = (email: string, password: string) =>
  client.post<AuthResponse>('/auth/register', { email, password });

export const login = (email: string, password: string) =>
  client.post<AuthResponse>('/auth/login', { email, password });
