import { Loader2 } from 'lucide-react';

export default function Spinner({ size = 'md' }: { size?: 'sm' | 'md' | 'lg' }) {
  const sizes = { sm: 16, md: 24, lg: 40 };

  return <Loader2 size={sizes[size]} className="animate-spin text-primary-600" />;
}
