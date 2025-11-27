import { Button } from "@/components/ui/button";
import { ButtonGroup } from "@/components/ui/button-group";
import type { Dispatch, SetStateAction } from "react";

interface setStartProp {
  startDate: number;
  setStartDate: Dispatch<SetStateAction<number>>;
}

const StartDateSelection = ({ startDate, setStartDate }: setStartProp) => {
  return (
    <ButtonGroup>
      <Button
        variant={startDate === 7 ? "default" : "ghost"}
        onClick={() => setStartDate(7)}
      >
        {" "}
        7 Days
      </Button>
      <Button
        variant={startDate === 30 ? "default" : "ghost"}
        onClick={() => setStartDate(30)}
      >
        {" "}
        30 Days
      </Button>
      <Button
        variant={startDate === 90 ? "default" : "ghost"}
        onClick={() => setStartDate(90)}
      >
        {" "}
        90 Days
      </Button>
    </ButtonGroup>
  );
};

export default StartDateSelection;
