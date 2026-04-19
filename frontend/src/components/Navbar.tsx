import { Car, LogOut } from 'lucide-react';
import { useAuth } from '../context/AuthContext';
import { useNavigate } from 'react-router-dom';

export default function Navbar() {
  const { email, signout } = useAuth();
  const navigate = useNavigate();

  const handleLogout = () => {
    signout();
    navigate('/login');
  };

  return (
    <nav className="bg-white border-b border-gray-200 sticky top-0 z-30">
      <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
        <div className="flex items-center justify-between h-16">
          <div className="flex items-center gap-3">
            <div className="w-9 h-9 rounded-lg bg-primary-600 flex items-center justify-center">
              <Car size={20} className="text-white" />
            </div>
            <span className="text-lg font-semibold text-gray-900">Ufinet Autos</span>
          </div>

          <div className="flex items-center gap-4">
            <span className="hidden sm:block text-sm text-gray-500">{email}</span>
            <button
              onClick={handleLogout}
              className="inline-flex items-center gap-1.5 px-3 py-1.5 text-sm font-medium text-gray-700 bg-gray-100 rounded-lg hover:bg-gray-200 transition-colors cursor-pointer"
            >
              <LogOut size={16} />
              Salir
            </button>
          </div>
        </div>
      </div>
    </nav>
  );
}
