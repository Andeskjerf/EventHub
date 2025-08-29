FROM docker.io/oven/bun:debian AS build
ARG VITE_API_BASE_URL=/api
ENV VITE_API_BASE_URL=$VITE_API_BASE_URL
ARG REGISTRATION_ENABLED=false
ENV VITE_REGISTRATION_ENABLED=$REGISTRATION_ENABLED
ARG ANONYMIZE_PAST_DAYS=7
ENV VITE_ANONYMIZE_PAST_DAYS=$ANONYMIZE_PAST_DAYS
WORKDIR /app

COPY package.json ./

RUN bun ci --only=production

COPY . .

RUN bun run build-only

FROM docker.io/nginx:alpine as production

COPY --from=build /app/dist /usr/share/nginx/html

COPY nginx.conf /etc/nginx/conf.d/default.conf

EXPOSE 80

CMD ["nginx", "-g", "daemon off;"]
