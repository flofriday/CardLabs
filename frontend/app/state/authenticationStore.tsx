import { create } from "zustand";

export const authenticationStore = create((set) => ({
  authenticated: true,
  setAuthenticated: (isAuthenticated: boolean) => {
    set({ authenticated: isAuthenticated });
  },
}));
