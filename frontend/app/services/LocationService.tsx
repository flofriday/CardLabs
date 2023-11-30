import { toast } from "react-toastify";

export async function getLocations(): Promise<string[]> {
  const response = await fetch("api/locations", {
    mode: "cors",
    method: "GET",
    headers: {
      Accept: "application/json",
    },
  });

  if (response.status !== 200) {
    toast.error(
      "Could not fetch locations as our servers seem to be too busy. Please try again."
    );
    return [];
  }

  const locations = (await response.json()) as string[];
  return locations;
}
