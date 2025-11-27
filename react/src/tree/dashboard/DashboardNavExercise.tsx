import {
  Command,
  CommandEmpty,
  CommandGroup,
  CommandInput,
  CommandItem,
  CommandList,
} from "@/components/ui/command";
import { capitalizeWords, type exercise } from "@/global/utils";
import { cn } from "@/lib/utils";
import { Check } from "lucide-react";
import { useState } from "react";
import { useNavigate } from "react-router-dom";

interface DashboardNavExerciseProp {
  exercises: exercise[];
}

const DashboardNavExercise = ({ exercises }: DashboardNavExerciseProp) => {
  const navigate = useNavigate();
  const value = useState("")[0];

  return (
    <Command className=" w-[200px]">
      <CommandInput placeholder="Search exercises..." className="h-9" />
      <CommandList>
        <CommandEmpty>No exercises found.</CommandEmpty>
        <CommandGroup>
          {exercises.map((ex) => (
            <CommandItem
              key={ex.id}
              value={ex.id}
              onSelect={(currentValue) => {
                navigate("exercises/" + currentValue.replace(" ", "-"));
              }}
            >
              {capitalizeWords(ex.id)}
              <Check
                className={cn(
                  "ml-auto",
                  value === ex.id ? "opacity-100" : "opacity-0"
                )}
              />
            </CommandItem>
          ))}
        </CommandGroup>
      </CommandList>
    </Command>
  );
};

export default DashboardNavExercise;
