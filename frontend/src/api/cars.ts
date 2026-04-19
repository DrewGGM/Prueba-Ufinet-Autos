import client from './client';
import type { CarRequest, PageResponse, Car } from '../types';

interface GetCarsParams {
  page?: number;
  size?: number;
  sortBy?: string;
  sortDirection?: string;
  search?: string;
  brand?: string;
  year?: number;
}

export const getCars = (params: GetCarsParams, signal?: AbortSignal) =>
  client.get<PageResponse<Car>>('/cars', { params, signal });

export const createCar = (data: CarRequest) =>
  client.post<Car>('/cars', data);

export const updateCar = (id: number, data: CarRequest) =>
  client.put<Car>(`/cars/${id}`, data);

export const deleteCar = (id: number) =>
  client.delete<void>(`/cars/${id}`);
