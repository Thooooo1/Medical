import { axiosClient } from "./axiosClient";

export const doctorApi = {
  getAll: () => axiosClient.get("/doctors"),

  getAppointments: (doctorId: number, date: string) =>
    axiosClient.get(`/doctors/${doctorId}/appointments`, {
      params: { date },
    }),
};
