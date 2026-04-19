import { useState, useEffect, useCallback } from 'react';
import toast from 'react-hot-toast';
import Navbar from '../components/Navbar';
import CarCard from '../components/CarCard';
import CarFilters from '../components/CarFilters';
import CarFormModal from '../components/CarFormModal';
import ConfirmDialog from '../components/ConfirmDialog';
import Pagination from '../components/Pagination';
import EmptyState from '../components/EmptyState';
import Spinner from '../components/Spinner';
import { getCars, createCar, updateCar, deleteCar } from '../api/cars';
import type { Car, CarRequest, CarFilters as Filters, PageResponse } from '../types';

export default function CarsPage() {
  const [page, setPage] = useState<PageResponse<Car> | null>(null);
  const [currentPage, setCurrentPage] = useState(0);
  const [filters, setFilters] = useState<Filters>({ search: '', brand: '', year: '' });
  const [loading, setLoading] = useState(true);

  // Modal state
  const [formOpen, setFormOpen] = useState(false);
  const [editingCar, setEditingCar] = useState<Car | null>(null);

  // Delete state
  const [deleteTarget, setDeleteTarget] = useState<Car | null>(null);
  const [deleting, setDeleting] = useState(false);

  const fetchCars = useCallback(async (signal?: AbortSignal) => {
    setLoading(true);
    try {
      const params: Record<string, string | number> = {
        page: currentPage,
        size: 12,
        sortBy: 'id',
        sortDirection: 'desc',
      };
      if (filters.search) params.search = filters.search;
      if (filters.brand) params.brand = filters.brand;
      if (filters.year) params.year = parseInt(filters.year);

      const { data } = await getCars(params, signal);
      setPage(data);
    } catch (err) {
      if (!signal?.aborted) {
        toast.error('Error al cargar los autos');
      }
    } finally {
      if (!signal?.aborted) {
        setLoading(false);
      }
    }
  }, [currentPage, filters]);

  useEffect(() => {
    const controller = new AbortController();
    fetchCars(controller.signal);
    return () => controller.abort();
  }, [fetchCars]);

  const handleFiltersChange = (newFilters: Filters) => {
    setFilters(newFilters);
    setCurrentPage(0);
  };

  const handleAdd = () => {
    setEditingCar(null);
    setFormOpen(true);
  };

  const handleEdit = (car: Car) => {
    setEditingCar(car);
    setFormOpen(true);
  };

  const handleSave = async (data: CarRequest) => {
    if (editingCar) {
      await updateCar(editingCar.id, data);
      toast.success('Auto actualizado');
    } else {
      await createCar(data);
      toast.success('Auto creado');
    }
    setFormOpen(false);
    setEditingCar(null);
    fetchCars();
  };

  const handleDelete = async () => {
    if (!deleteTarget) return;
    setDeleting(true);
    try {
      await deleteCar(deleteTarget.id);
      toast.success('Auto eliminado');
      setDeleteTarget(null);
      fetchCars();
    } catch {
      toast.error('Error al eliminar');
    } finally {
      setDeleting(false);
    }
  };

  return (
    <div className="min-h-screen bg-gray-50">
      <Navbar />

      <main className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-8">
        {/* Header */}
        <div className="mb-6">
          <h1 className="text-2xl font-bold text-gray-900">Mis autos</h1>
          <p className="text-sm text-gray-500 mt-1">
            {page ? `${page.totalElements} auto${page.totalElements !== 1 ? 's' : ''} registrado${page.totalElements !== 1 ? 's' : ''}` : 'Cargando...'}
          </p>
        </div>

        {/* Filters */}
        <div className="mb-6">
          <CarFilters filters={filters} onFiltersChange={handleFiltersChange} onAdd={handleAdd} />
        </div>

        {/* Content */}
        {loading ? (
          <div className="flex items-center justify-center py-20">
            <Spinner size="lg" />
          </div>
        ) : page && page.content.length > 0 ? (
          <>
            <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 xl:grid-cols-4 gap-4">
              {page.content.map((car) => (
                <CarCard
                  key={car.id}
                  car={car}
                  onEdit={handleEdit}
                  onDelete={setDeleteTarget}
                />
              ))}
            </div>
            <Pagination
              page={currentPage}
              totalPages={page.totalPages}
              totalElements={page.totalElements}
              size={page.size}
              onPageChange={setCurrentPage}
            />
          </>
        ) : (
          <EmptyState
            title={filters.search || filters.brand || filters.year ? 'Sin resultados' : 'Sin autos'}
            description={
              filters.search || filters.brand || filters.year
                ? 'No se encontraron autos con los filtros aplicados.'
                : 'Aun no has registrado ningun auto. Agrega tu primer auto para comenzar.'
            }
            action={
              !(filters.search || filters.brand || filters.year)
                ? { label: 'Agregar auto', onClick: handleAdd }
                : undefined
            }
          />
        )}
      </main>

      {/* Modals */}
      <CarFormModal
        open={formOpen}
        car={editingCar}
        onSave={handleSave}
        onClose={() => {
          setFormOpen(false);
          setEditingCar(null);
        }}
      />

      <ConfirmDialog
        open={!!deleteTarget}
        title="Eliminar auto"
        message={
          deleteTarget
            ? `Estas seguro de eliminar el ${deleteTarget.brand} ${deleteTarget.model} (${deleteTarget.licensePlate})?`
            : ''
        }
        onConfirm={handleDelete}
        onCancel={() => setDeleteTarget(null)}
        loading={deleting}
      />
    </div>
  );
}
