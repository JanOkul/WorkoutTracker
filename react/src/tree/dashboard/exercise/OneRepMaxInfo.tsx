import {
  HoverCard,
  HoverCardContent,
  HoverCardTrigger,
} from "@/components/ui/hover-card";
import { Separator } from "@/components/ui/separator";

import { Info } from "lucide-react";

const OneRepMaxInfo = () => {
  return (
    <div className="flex ml-auto items-center">
      <HoverCard openDelay={150} closeDelay={0}>
        <HoverCardTrigger>
          <Info size={18} className="hover:bg-gray-300 rounded-full" />
        </HoverCardTrigger>
        <HoverCardContent side="top" align="end" className="w-120">
          <h4>Calculation</h4>
          <ul>
            <li>Each point represents a workout day for this exercise.</li>
            <li>1RM is estimated using the Epley formula for each set.</li>
            <Separator className="mt-2 mb-2" />
            <li>
              <strong>Average 1RM:</strong> Mean of all sets that day.
            </li>
            <li>
              <strong>Max 1RM:</strong> Best set that day.
            </li>
          </ul>
        </HoverCardContent>
      </HoverCard>
    </div>
  );
};

export default OneRepMaxInfo;
