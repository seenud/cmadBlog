# Extend vert.x image
FROM vertx/vertx3
#
#                                                       
ENV VERTICLE_NAME com.cisco.training.cmad.verticles.BootStrapVerticle
ENV VERTICLE_FILE target/cmadBlog-1.0.0-SNAPSHOT.jar

# Set the location of the verticles
ENV VERTICLE_HOME /usr/verticles

EXPOSE 8080

# Copy your verticle to the container                   
COPY $VERTICLE_FILE $VERTICLE_HOME/

# Launch the verticle
WORKDIR $VERTICLE_HOME
ENTRYPOINT ["sh", "-c"]
CMD ["vertx run $VERTICLE_NAME -cp $VERTICLE_HOME/*"]
