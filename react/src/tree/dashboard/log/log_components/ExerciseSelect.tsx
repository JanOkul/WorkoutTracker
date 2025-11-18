"use client";

import { Check, ChevronsUpDown } from "lucide-react";
import * as React from "react";

import { Button } from "@/components/ui/button";
import {
  Command,
  CommandEmpty,
  CommandGroup,
  CommandInput,
  CommandItem,
  CommandList,
} from "@/components/ui/command";
import {
  Popover,
  PopoverContent,
  PopoverTrigger,
} from "@/components/ui/popover";
import { capitalizeWords, type exercise } from "@/global/utils";
import { cn } from "@/lib/utils";

interface ExerciseSelectProps {
  exercises: exercise[];
  value: string;
  onChange: (value: string) => void;
}

export const ExerciseSelect = ({
  exercises,
  value,
  onChange,
}: ExerciseSelectProps) => {
  const [open, setOpen] = React.useState(false);

  return (
    <Popover open={open} onOpenChange={setOpen}>
      <PopoverTrigger asChild>
        <Button
          variant="outline"
          role="combobox"
          aria-expanded={open}
          className="w-full justify-between" // full width
        >
          {value
            ? capitalizeWords(exercises.find((ex) => ex.id === value)?.id || "")
            : "Select exercise..."}
          <ChevronsUpDown className="opacity-50" />
        </Button>
      </PopoverTrigger>

      <PopoverContent className="w-full p-0">
        <Command>
          <CommandInput placeholder="Search exercises..." className="h-9" />
          <CommandList>
            <CommandEmpty>No exercises found.</CommandEmpty>
            <CommandGroup>
              {exercises.map((ex) => (
                <CommandItem
                  key={ex.id}
                  value={ex.id}
                  onSelect={(currentValue) => {
                    onChange(currentValue === value ? "" : currentValue);
                    setOpen(false);
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
      </PopoverContent>
    </Popover>
  );
};

export default ExerciseSelect;
