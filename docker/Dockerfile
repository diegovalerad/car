FROM payara/server-full:4.181

#Setup configuration
USER payara
COPY ./postgres/postgresql-42.2.8.jre7.jar /opt/payara41/glassfish/domains/domain1/lib
COPY ./payara/domain.xml /opt/payara41/glassfish/domains/domain1/config
