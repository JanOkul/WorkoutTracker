import React, {
  createContext,
  useContext,
  useState,
  type ReactNode,
} from "react";
import type { exercise } from "./utils";

interface ExerciseListContextType {
  items: exercise[];
  setItems: React.Dispatch<React.SetStateAction<exercise[]>>;
}

const ExerciseListContext = createContext<ExerciseListContextType | undefined>(
  undefined
);

interface ProviderProps {
  children: ReactNode;
}

export function ExerciseListProvider({ children }: ProviderProps) {
  const [items, setItems] = useState<exercise[]>([]);

  const value: ExerciseListContextType = {
    items,
    setItems,
  };

  return (
    <ExerciseListContext.Provider value={value}>
      {children}
    </ExerciseListContext.Provider>
  );
}

export function useExerciseList(): ExerciseListContextType {
  const context = useContext(ExerciseListContext);
  if (!context) {
    throw new Error(
      "useExerciseList must be used within a ExerciseListProvider"
    );
  }
  return context;
}
