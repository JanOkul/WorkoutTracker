import type { ReactNode } from "react";

interface TileProps {
  children?: ReactNode;
  className?: string;
  onClick?: () => void;
  title?: string;
  subtitle?: string;
}

const Tile = ({
  children,
  className = "border-2 rounded-lg p-4 min-h-50 md:flex-1",
  onClick,
  title,
  subtitle,
}: TileProps) => {
  return (
    <div onClick={onClick} className={className}>
      {(title || subtitle) && (
        <div>
          {title && <h2>{title}</h2>}
          {subtitle && <p>{subtitle}</p>}
        </div>
      )}

      <div>{children}</div>
    </div>
  );
};

export default Tile;
