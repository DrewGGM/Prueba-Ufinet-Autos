export interface AuthResponse {
  token: string;
  email: string;
}

export interface Car {
  id: number;
  brand: string;
  model: string;
  year: number;
  licensePlate: string;
  color: string;
  photoUrl: string | null;
}

export interface CarRequest {
  brand: string;
  model: string;
  year: number;
  licensePlate: string;
  color: string;
  photoUrl: string | null;
}

export interface PageResponse<T> {
  content: T[];
  page: number;
  size: number;
  totalElements: number;
  totalPages: number;
  last: boolean;
}

export interface CarFilters {
  search: string;
  brand: string;
  year: string;
}
