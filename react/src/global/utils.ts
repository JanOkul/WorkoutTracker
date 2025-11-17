export const capitalizeWords = (s: string): string => {
  console.log("String to format: " + s);
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
