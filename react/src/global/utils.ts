export const capitalizeWords = (s: string): string => {
  if (!s || s.trim().length === 0) {
    return s;
  }

  return s
    .split(" ")
    .map((word) => {
      if (!word) return word;
      return word.charAt(0).toUpperCase() + word.slice(1).toLowerCase();
    })
    .join(" ");
};

export interface exercise {
  id: string;
  description: string;
}

export interface exerciselist {
  exercises: exercise[];
}
