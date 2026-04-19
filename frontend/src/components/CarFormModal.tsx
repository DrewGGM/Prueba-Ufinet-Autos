import { useState, useEffect } from 'react';
import { X } from 'lucide-react';
import type { Car, CarRequest } from '../types';
import Spinner from './Spinner';

interface CarFormModalProps {
  open: boolean;
  car: Car | null;
  onSave: (data: CarRequest) => Promise<void>;
  onClose: () => void;
}

const emptyForm: CarRequest = {
  brand: '',
  model: '',
  year: new Date().getFullYear(),
  licensePlate: '',
  color: '',
  photoUrl: null,
};

export default function CarFormModal({ open, car, onSave, onClose }: CarFormModalProps) {
  const [form, setForm] = useState<CarRequest>(emptyForm);
  const [errors, setErrors] = useState<Record<string, string>>({});
  const [loading, setLoading] = useState(false);

  const isEditing = !!car;

  useEffect(() => {
    if (car) {
      setForm({
        brand: car.brand,
        model: car.model,
        year: car.year,
        licensePlate: car.licensePlate,
        color: car.color,
        photoUrl: car.photoUrl,
      });
    } else {
      setForm(emptyForm);
    }
    setErrors({});
  }, [car, open]);

  const validate = (): boolean => {
    const errs: Record<string, string> = {};
    if (!form.brand.trim()) errs.brand = 'La marca es obligatoria';
    if (!form.model.trim()) errs.model = 'El modelo es obligatorio';
    if (form.year < 1900 || form.year > 2100) errs.year = 'Ano invalido';
    if (!form.licensePlate.trim()) errs.licensePlate = 'La placa es obligatoria';
    if (!form.color.trim()) errs.color = 'El color es obligatorio';
    setErrors(errs);
    return Object.keys(errs).length === 0;
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    if (!validate()) return;
    setLoading(true);
    try {
      await onSave(form);
    } catch (err: unknown) {
      const error = err as { response?: { data?: { errors?: Record<string, string>; message?: string } } };
      if (error.response?.data?.errors) {
        setErrors(error.response.data.errors);
      } else if (error.response?.data?.message) {
        setErrors({ _general: error.response.data.message });
      }
    } finally {
      setLoading(false);
    }
  };

  const set = (field: keyof CarRequest, value: string | number | null) =>
    setForm((prev) => ({ ...prev, [field]: value }));

  if (!open) return null;

  return (
    <div className="fixed inset-0 z-50 flex items-center justify-center p-4">
      <div className="fixed inset-0 bg-black/40 backdrop-blur-sm" onClick={onClose} />
      <div className="relative bg-white rounded-xl shadow-xl max-w-lg w-full max-h-[90vh] overflow-y-auto">
        <div className="sticky top-0 bg-white border-b border-gray-100 px-6 py-4 flex items-center justify-between rounded-t-xl">
          <h2 className="text-lg font-semibold text-gray-900">
            {isEditing ? 'Editar auto' : 'Nuevo auto'}
          </h2>
          <button onClick={onClose} className="p-1 text-gray-400 hover:text-gray-600 transition-colors cursor-pointer">
            <X size={20} />
          </button>
        </div>

        <form onSubmit={handleSubmit} className="p-6 space-y-4">
          {errors._general && (
            <div className="p-3 bg-red-50 border border-red-200 rounded-lg text-sm text-red-700">
              {errors._general}
            </div>
          )}

          <div className="grid grid-cols-2 gap-4">
            <div>
              <label className="block text-sm font-medium text-gray-700 mb-1">Marca</label>
              <input
                type="text"
                value={form.brand}
                onChange={(e) => set('brand', e.target.value)}
                placeholder="Toyota"
                className={`w-full rounded-lg border px-3 py-2.5 text-sm outline-none transition-colors focus:border-primary-500 focus:ring-2 focus:ring-primary-500/20 ${errors.brand ? 'border-red-400' : 'border-gray-300'}`}
                autoFocus
              />
              {errors.brand && <p className="mt-1 text-xs text-red-500">{errors.brand}</p>}
            </div>
            <div>
              <label className="block text-sm font-medium text-gray-700 mb-1">Modelo</label>
              <input
                type="text"
                value={form.model}
                onChange={(e) => set('model', e.target.value)}
                placeholder="Corolla"
                className={`w-full rounded-lg border px-3 py-2.5 text-sm outline-none transition-colors focus:border-primary-500 focus:ring-2 focus:ring-primary-500/20 ${errors.model ? 'border-red-400' : 'border-gray-300'}`}
              />
              {errors.model && <p className="mt-1 text-xs text-red-500">{errors.model}</p>}
            </div>
          </div>

          <div className="grid grid-cols-2 gap-4">
            <div>
              <label className="block text-sm font-medium text-gray-700 mb-1">Ano</label>
              <input
                type="number"
                value={form.year}
                onChange={(e) => set('year', parseInt(e.target.value) || 0)}
                min={1900}
                max={2100}
                className={`w-full rounded-lg border px-3 py-2.5 text-sm outline-none transition-colors focus:border-primary-500 focus:ring-2 focus:ring-primary-500/20 ${errors.year ? 'border-red-400' : 'border-gray-300'}`}
              />
              {errors.year && <p className="mt-1 text-xs text-red-500">{errors.year}</p>}
            </div>
            <div>
              <label className="block text-sm font-medium text-gray-700 mb-1">Color</label>
              <input
                type="text"
                value={form.color}
                onChange={(e) => set('color', e.target.value)}
                placeholder="Blanco"
                className={`w-full rounded-lg border px-3 py-2.5 text-sm outline-none transition-colors focus:border-primary-500 focus:ring-2 focus:ring-primary-500/20 ${errors.color ? 'border-red-400' : 'border-gray-300'}`}
              />
              {errors.color && <p className="mt-1 text-xs text-red-500">{errors.color}</p>}
            </div>
          </div>

          <div>
            <label className="block text-sm font-medium text-gray-700 mb-1">Placa</label>
            <input
              type="text"
              value={form.licensePlate}
              onChange={(e) => set('licensePlate', e.target.value.toUpperCase())}
              placeholder="ABC-1234"
              className={`w-full rounded-lg border px-3 py-2.5 text-sm font-mono tracking-wider outline-none transition-colors focus:border-primary-500 focus:ring-2 focus:ring-primary-500/20 ${errors.licensePlate ? 'border-red-400' : 'border-gray-300'}`}
            />
            {errors.licensePlate && <p className="mt-1 text-xs text-red-500">{errors.licensePlate}</p>}
          </div>

          <div>
            <label className="block text-sm font-medium text-gray-700 mb-1">
              URL de foto <span className="text-gray-400 font-normal">(opcional)</span>
            </label>
            <input
              type="url"
              value={form.photoUrl ?? ''}
              onChange={(e) => set('photoUrl', e.target.value || null)}
              placeholder="https://ejemplo.com/foto.jpg"
              className="w-full rounded-lg border border-gray-300 px-3 py-2.5 text-sm outline-none transition-colors focus:border-primary-500 focus:ring-2 focus:ring-primary-500/20"
            />
          </div>

          <div className="flex justify-end gap-3 pt-2">
            <button
              type="button"
              onClick={onClose}
              disabled={loading}
              className="px-4 py-2.5 text-sm font-medium text-gray-700 bg-gray-100 rounded-lg hover:bg-gray-200 transition-colors cursor-pointer disabled:opacity-50"
            >
              Cancelar
            </button>
            <button
              type="submit"
              disabled={loading}
              className="inline-flex items-center gap-2 px-5 py-2.5 text-sm font-medium text-white bg-primary-600 rounded-lg hover:bg-primary-700 transition-colors cursor-pointer disabled:opacity-50"
            >
              {loading && <Spinner size="sm" />}
              {isEditing ? 'Guardar cambios' : 'Crear auto'}
            </button>
          </div>
        </form>
      </div>
    </div>
  );
}
