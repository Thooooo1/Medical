import { axiosClient } from "./axiosClient";

export const appointmentApi = {
  book: (params: {
    userId: number;
    doctorId: number;
    time: string;
    note?: string;
  }) =>
    axiosClient.post("/appointments", null, {
      params,
    }),

  getFreeSlots: (doctorId: number, date: string) =>
    axiosClient.get("/appointments/free-slots", {
      params: { doctorId, date },
    }),
};
