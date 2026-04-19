import { CarFront, Hash, Pencil, Trash2 } from 'lucide-react';
import type { Car } from '../types';

interface CarCardProps {
  car: Car;
  onEdit: (car: Car) => void;
  onDelete: (car: Car) => void;
}

const colorDot: Record<string, string> = {
  blanco: 'bg-white border border-gray-300',
  negro: 'bg-gray-900',
  rojo: 'bg-red-500',
  azul: 'bg-blue-500',
  gris: 'bg-gray-400',
  plata: 'bg-gray-300',
  verde: 'bg-green-500',
  amarillo: 'bg-yellow-400',
  naranja: 'bg-orange-500',
};

function getColorClass(color: string) {
  return colorDot[color.toLowerCase()] ?? 'bg-primary-400';
}

export default function CarCard({ car, onEdit, onDelete }: CarCardProps) {
  return (
    <div className="bg-white rounded-xl border border-gray-200 overflow-hidden hover:shadow-md transition-shadow group">
      {/* Photo placeholder */}
      <div className="h-40 bg-gradient-to-br from-gray-100 to-gray-50 flex items-center justify-center relative">
        {car.photoUrl ? (
          <img
            src={car.photoUrl}
            alt={`${car.brand} ${car.model}`}
            className="w-full h-full object-cover"
            onError={(e) => {
              (e.target as HTMLImageElement).style.display = 'none';
              (e.target as HTMLImageElement).nextElementSibling?.classList.remove('hidden');
            }}
          />
        ) : null}
        <div className={`flex flex-col items-center gap-2 text-gray-300 ${car.photoUrl ? 'hidden' : ''}`}>
          <CarFront size={56} strokeWidth={1} />
          <span className="text-xs">Sin foto</span>
        </div>
        <span className="absolute top-3 right-3 bg-white/90 backdrop-blur text-xs font-medium text-gray-700 px-2 py-1 rounded-full">
          {car.year}
        </span>
      </div>

      {/* Content */}
      <div className="p-4">
        <div className="flex items-start justify-between mb-2">
          <div>
            <h3 className="font-semibold text-gray-900">{car.brand}</h3>
            <p className="text-sm text-gray-500">{car.model}</p>
          </div>
          <div className="flex items-center gap-1.5">
            <span className={`w-3 h-3 rounded-full ${getColorClass(car.color)}`} />
            <span className="text-xs text-gray-500 capitalize">{car.color}</span>
          </div>
        </div>

        <div className="flex items-center gap-1.5 mb-4 text-sm text-gray-600 bg-gray-50 rounded-lg px-3 py-2">
          <Hash size={16} className="text-gray-400 shrink-0" />
          <span className="font-mono tracking-wider">{car.licensePlate}</span>
        </div>

        {/* Actions */}
        <div className="flex gap-2">
          <button
            onClick={() => onEdit(car)}
            className="flex-1 inline-flex items-center justify-center gap-1.5 px-3 py-2 text-sm font-medium text-primary-700 bg-primary-50 rounded-lg hover:bg-primary-100 transition-colors cursor-pointer"
          >
            <Pencil size={16} />
            Editar
          </button>
          <button
            onClick={() => onDelete(car)}
            className="inline-flex items-center justify-center gap-1.5 px-3 py-2 text-sm font-medium text-red-700 bg-red-50 rounded-lg hover:bg-red-100 transition-colors cursor-pointer"
          >
            <Trash2 size={16} />
          </button>
        </div>
      </div>
    </div>
  );
}
