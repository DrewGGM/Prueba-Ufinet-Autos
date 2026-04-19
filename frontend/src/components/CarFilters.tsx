import { useState, useEffect } from 'react';
import { Search, Plus, X } from 'lucide-react';
import type { CarFilters as Filters } from '../types';

interface CarFiltersProps {
  filters: Filters;
  onFiltersChange: (filters: Filters) => void;
  onAdd: () => void;
}

export default function CarFilters({ filters, onFiltersChange, onAdd }: CarFiltersProps) {
  const [local, setLocal] = useState(filters);

  useEffect(() => {
    setLocal(filters);
  }, [filters]);

  useEffect(() => {
    const timeout = setTimeout(() => {
      if (
        local.search !== filters.search ||
        local.brand !== filters.brand ||
        local.year !== filters.year
      ) {
        onFiltersChange(local);
      }
    }, 400);
    return () => clearTimeout(timeout);
  }, [local]);

  const hasFilters = filters.search || filters.brand || filters.year;

  const clearAll = () => {
    const cleared = { search: '', brand: '', year: '' };
    setLocal(cleared);
    onFiltersChange(cleared);
  };

  return (
    <div className="space-y-3">
      <div className="flex flex-col sm:flex-row items-start sm:items-center gap-3">
        {/* Search */}
        <div className="relative flex-1 w-full">
          <Search size={16} className="absolute left-3 top-1/2 -translate-y-1/2 text-gray-400" />
          <input
            type="text"
            value={local.search}
            onChange={(e) => setLocal({ ...local, search: e.target.value })}
            placeholder="Buscar por placa o modelo..."
            className="w-full pl-10 pr-4 py-2.5 text-sm border border-gray-300 rounded-lg outline-none focus:border-primary-500 focus:ring-2 focus:ring-primary-500/20 transition-colors"
          />
        </div>

        {/* Brand filter */}
        <input
          type="text"
          value={local.brand}
          onChange={(e) => setLocal({ ...local, brand: e.target.value })}
          placeholder="Filtrar marca"
          className="w-full sm:w-40 px-3 py-2.5 text-sm border border-gray-300 rounded-lg outline-none focus:border-primary-500 focus:ring-2 focus:ring-primary-500/20 transition-colors"
        />

        {/* Year filter */}
        <input
          type="number"
          value={local.year}
          onChange={(e) => setLocal({ ...local, year: e.target.value })}
          placeholder="Ano"
          min={1900}
          max={2100}
          className="w-full sm:w-28 px-3 py-2.5 text-sm border border-gray-300 rounded-lg outline-none focus:border-primary-500 focus:ring-2 focus:ring-primary-500/20 transition-colors"
        />

        {/* Add button */}
        <button
          onClick={onAdd}
          className="w-full sm:w-auto inline-flex items-center justify-center gap-2 px-5 py-2.5 text-sm font-medium text-white bg-primary-600 rounded-lg hover:bg-primary-700 transition-colors cursor-pointer whitespace-nowrap"
        >
          <Plus size={16} />
          Nuevo auto
        </button>
      </div>

      {hasFilters && (
        <button
          onClick={clearAll}
          className="inline-flex items-center gap-1 text-xs text-primary-600 hover:text-primary-700 transition-colors cursor-pointer"
        >
          <X size={14} />
          Limpiar filtros
        </button>
      )}
    </div>
  );
}
